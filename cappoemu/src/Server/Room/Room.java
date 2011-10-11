package Server.Room;

import Server.Connection;
import Server.DatabaseClient;
import Server.MoodlightData;
import Server.Pet;
import Server.PetBot;
import Server.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Point;

import Server.Server;
import Server.Room.RoomModel.SquareState;
import Server.Rotation;
import Server.ServerMessage;
import Server.TraxPlaylist;
import Server.WiredManager;
import java.sql.ResultSet;

/*
 *****************
 * @author capos *
 *****************
*/

public class Room extends Thread
{    
    public void Serialize(ServerMessage ClientMessage)
    {
        Environment.Append(Id, ClientMessage);
        if(Event != null)
        {
            Environment.Append(true, ClientMessage); // is event
            Environment.Append(Event.Name, ClientMessage);
        }
        else
        {
            Environment.Append(false, ClientMessage); // is event
            Environment.Append(Name, ClientMessage);
        }
        Environment.Append(Owner, ClientMessage);
        Environment.Append(State, ClientMessage); // room state
        Environment.Append(UsersNow, ClientMessage);
        Environment.Append(UsersMax, ClientMessage);
        Environment.Append(Description, ClientMessage);
        Environment.Append(0, ClientMessage); // ?
        if(Event != null)
        {
            Environment.Append(false, ClientMessage); // can trade
            Environment.Append(Score, ClientMessage);
            Environment.Append(Event.Category, ClientMessage);
            Environment.Append(Event.StartTime, ClientMessage);
        }
        else
        {
            Environment.Append(true, ClientMessage); // can trade
            Environment.Append(Score, ClientMessage);
            Environment.Append(Category, ClientMessage);
            Environment.Append("", ClientMessage);
        }
        Environment.Append(Tags.size(), ClientMessage);
        for(String Tag : Tags)
        {
            Environment.Append(Tag, ClientMessage);
        }
        Environment.Append(Icon.BackgroundImage, ClientMessage);
        Environment.Append(Icon.ForegroundImage, ClientMessage);
        Environment.Append(Icon.Items.length, ClientMessage);
        for(int ItemId = 0;ItemId<Icon.Items.length;ItemId++)
        {
            String[] values = Icon.Items[ItemId].split(",");
            Environment.Append(Integer.parseInt(values[0]), ClientMessage);
            Environment.Append(Integer.parseInt(values[1]), ClientMessage);
        }
        Environment.Append((Flags & Server.rmAllowPets) == Server.rmAllowPets, ClientMessage); // AllowPets
        Environment.Append(true, ClientMessage); // allow Ad
    }
    
    public enum MatrixState
    {
        BLOCKED,
        WALKABLE,
        WALKABLE_LASTSTEP,
        SENTAR,
        ACOSTAR
    }

    public Server Environment;

    public int OwnerId;
    public int Id;
    public int State;
    public int Category;
    public int UsersNow;
    public int UsersMax;
    public int PetCounter = 0;
    public int BotCounter = 0;
    public int VirtualIdCounter = 1;
    public int Score;
    public int WallAnchor = 0;
    public int FloorAnchor = 0;
    public int BB_Points_R;
    public int BB_Points_G;
    public int BB_Points_B;
    public int BB_Points_Y;
    public int Timer_Steeps = 0;

    public String Name;
    public String Description;
    public String Owner;
    public String ModelName;
    public String Password;
    public String Wallpaper;
    public String Floor;
    public String Landscape;

    public int Flags;

    public RoomIcon Icon;
    public RoomModel Model;
    public RoomEvent Event;
    public WiredManager WiredManager;
    public MoodlightData MoodlightData;

    public boolean[][] UserMatrix;
    public int[][] MatrixRot;
    private int[][] BB_Squares;
    public double[][] HeightMatrix;
    public double[][] TopStackHeight;
    public Coord[][] BedMatrix;
    public MatrixState[][] Matrix;
    public RoomUser[] UserList = new RoomUser[UsersMax+6]; // + 1 bot y 5 pets

    public Map<Integer, Integer> Bans = new HashMap<Integer, Integer>();
    public List<RoomItem> PendingFloorItems = new ArrayList<RoomItem>();
    public List<RoomItem> FloorItems = new ArrayList<RoomItem>(100);
    public List<RoomItem> WallItems = new ArrayList<RoomItem>(100);
    public List<Integer> UsersWithRights = new ArrayList<Integer>();
    public List<String> Tags = new ArrayList<String>();
    public TraxPlaylist TraxPlaylist = new TraxPlaylist();


    public int[][] BETA_ListId;
    public List<List<RoomItem>> BETA_ItemsContainer = new ArrayList<List<RoomItem>>();

    private boolean FloorItemsBusy;
    private boolean Running = false;

    private List<Point> Squares = new ArrayList<Point>();

    public void StopRunning()
    {
        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(18, Message);
        SendMessage(Message);
        Running = false;
    }

    public final void SetFlag(int Flag, boolean Add)
    {
        if(Add)
        {
            if((Flags & Flag) != Flag)
            {
                Flags |= Flag;
            }
        }
        else
        {
            Flags &= ~Flag;
        }
    }

    public Room(Server Environment)
    {
        this.Environment = Environment;
        this.WiredManager = new WiredManager(this,Environment);
    }

    public RoomItem GetItem(int Id)
    {
        for(RoomItem Item : FloorItems)
        {
            if (Item.Id == Id)
            {
                return Item;
            }
        }

        for(RoomItem Item : WallItems)
        {
            if (Item.Id == Id)
            {
                return Item;
            }
        }

        return null;
    }

    public RoomItem GetFloorItem(int Id)
    {
        for(RoomItem Item : FloorItems)
        {
            if (Item.Id == Id)
            {
                return Item;
            }
        }

        return null;
    }

    public RoomItem GetWallItem(int Id)
    {
        for(RoomItem Item : WallItems)
        {
            if (Item.Id == Id)
            {
                return Item;
            }
        }
        return null;
    }

    public void RemoveFurniture(Connection User, RoomItem Item)
    {
        if (Item.IsWallItem)
        {
            ServerMessage Message = new ServerMessage();
            Environment.InitPacket(84, Message);
            Environment.Append(Integer.toString(Item.Id), Message);
            SendMessage(Message);

            WallItems.remove(Item);
        }
        else if (Item.IsFloorItem)
        {
            ServerMessage Message = new ServerMessage();
            Environment.InitPacket(94, Message);
            Environment.Append(Integer.toString(Item.Id), Message);
            SendMessage(Message);

            List<RoomItem> container = BETA_ItemsContainer.get(BETA_ListId[Item.X][Item.Y]);
            container.remove(Item);
            GenerateSquare(Item.X, Item.Y);

            Map<Integer, AffectedTile> PointList = GetAffectedTiles(Item.BaseItem.Length, Item.BaseItem.Width, Item.X, Item.Y, Item.Rot);

            for(AffectedTile Tile : PointList.values())
            {
                container = BETA_ItemsContainer.get(BETA_ListId[Tile.X][Tile.Y]);
                container.remove(Item);
                GenerateSquare(Tile.X, Tile.Y);
            }

            FloorItems.remove(Item);
        }

        Item.BaseItem.Interactor.OnRemove(User, Item);

        int count = UserList.length;

        for (int i = 0; i < count; i++)
        {
            if (UserList[i] != null)
            {
                UpdateUserStatus(UserList[i], true, null, true);
            }
        }
    }

    public boolean SetWallItem(Connection User, RoomItem RoomItem, boolean newItem)
    {
        if(!newItem)
        {
            RemoveFurniture(User, RoomItem);
        }

        RoomItem.BaseItem.Interactor.OnPlace(User, RoomItem);

        if(RoomItem.BaseItem.Interaction == Environment.Furniture.DIMMER)
        {
            if(MoodlightData == null)
            {
                MoodlightData = new MoodlightData();
                MoodlightData.Enabled = false;
                MoodlightData.CurrentPreset = 1;
                MoodlightData.AddPresent("#000000,255,0");
                MoodlightData.AddPresent("#000000,255,0");
                MoodlightData.AddPresent("#000000,255,0");
                MoodlightData.ItemId = RoomItem.Id;
                RoomItem.ExtraData = MoodlightData.GenerateExtraData();
            }
        }

        RoomItem.IsWallItem = true;
        WallItems.add(RoomItem);

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(83,Message);
        SerializeWall(RoomItem, Message);
        SendMessage(Message);

        RoomItem.UpdateNeeded = true;

        return true;
    }

    public boolean CheckSq(RoomItem Item, int newX, int newY)
    {
        // Verify tiles are valid
        if (!ValidTile(newX, newY))
        {
            return false;
        }

        // es un cuadro valido?
        if (Model.SqState[newX][newY] != SquareState.OPEN)
        {
            return false;
        }

        // contiene usuarios el cuadro?
        if (SquareHasUsers(newX, newY))
        {
            return false;
        }

        // calcular la posicion en cuanto a altura dependiendo de el model de la sala..
        Double newZ = Model.SqFloorHeight[newX][newY];

        List<RoomItem> ItemsAffected = GetFurniObjects(newX, newY);

        for(RoomItem I : ItemsAffected)
        {
            if (I.Id == Item.Id)
            {
                continue; // omitirse a si mismo
            }

            if (!Item.BaseItem.Walkable)
            {
                return false;
            }

            double altura = (I.Z+I.BaseItem.Height);
            if (altura > newZ) // encontrar el punto mas alto
            {
                newZ = altura; // punto mas alto

                if (newZ > 10.0) // es muy alto?
                {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean SetFloorItem(Connection User, RoomItem Item, int newX, int newY, int newRot, boolean newItem)
    {
        // Verify tiles are valid
        if (!ValidTile(newX, newY))
        {
            return false;
        }

        // Verify the rotation is correct
        if (newRot != 0 && newRot != 2 && newRot != 4 && newRot != 6 && newRot != 8)
        {
            newRot = 0;
        }

        if (!newItem)
        {
            // cuando le dan en mover al mismo lugar donde se encuentra..
            if (Item.Rot == newRot && Item.X == newX && Item.Y == newY)
            {
                return false;
            }
        }

        // encontrar los cuadros que usara el item
        Map<Integer, AffectedTile> Points = GetAffectedTiles(Item.BaseItem.Length, Item.BaseItem.Width, newX, newY, newRot);

        for(AffectedTile Tile : Points.values())
        {
            if (!ValidTile(Tile.X, Tile.Y))
            {
                return false;
            }
        }

        // calcular la posicion en cuanto a altura dependiendo de el model de la sala..
        Double newZ = Model.SqFloorHeight[newX][newY];

        if (Item.BaseItem.IsSeat)
        {
            List<RoomItem> ItemsAffected = new ArrayList<RoomItem>();

            // revisar cuadros afectados por el item
            for(AffectedTile Tile : Points.values())
            {
                // encontrar items en el cuadro
                List<RoomItem> Temp = GetFurniObjects(Tile.X, Tile.Y);
                if (Temp != null)
                {
                    ItemsAffected.addAll(Temp);
                }
            }

            // encontrar items en el cuadro
            List<RoomItem> ItemsOnTile = GetFurniObjects(newX, newY);

            // Lista completa de items encontrados en cuadros afectados
            List<RoomItem> ItemsComplete = new ArrayList<RoomItem>();
            if (ItemsOnTile != null)
            {
                ItemsComplete.addAll(ItemsOnTile);
            }
            ItemsComplete.addAll(ItemsAffected);

            for(RoomItem I : ItemsComplete)
            {
                if (I.Id == Item.Id)
                {
                    continue; // omitirse a si mismo
                }

                if (!I.BaseItem.Walkable) // las camas y sofas solo pueden estar sobre items walkable
                {
                    return false;
                }

                double altura = (I.Z+I.BaseItem.Height);
                if (altura > newZ) // encontrar el punto mas alto
                {
                    newZ = altura; // punto mas alto

                    if (newZ > 10.0) // es muy alto?
                    {
                        return false;
                    }
                }
            }
        }
        else
        {
            // es un cuadro valido?
            if (Model.SqState[newX][newY] != SquareState.OPEN)
            {
                return false;
            }

            // revisar cuadros afectados por el item
            for(AffectedTile Tile : Points.values())
            {
                // es un cuadro valido?
                if (Model.SqState[Tile.X][Tile.Y] != SquareState.OPEN)
                {
                    return false;
                }
            }

            // Rotacion del item
            if (Item.Rot != newRot && Item.X == newX && Item.Y == newY)
            {
                newZ = Item.Z;
            }
            else
            {
                if(Item.BaseItem.Walkable)
                {
                    List<RoomItem> ItemsAffected = new ArrayList<RoomItem>();

                    // revisar cuadros afectados por el item
                    for(AffectedTile Tile : Points.values())
                    {
                        // encontrar items en el cuadro
                        List<RoomItem> Temp = GetFurniObjects(Tile.X, Tile.Y);
                        if (Temp != null)
                        {
                            ItemsAffected.addAll(Temp);
                        }
                    }

                    // encontrar items en el cuadro
                    List<RoomItem> ItemsOnTile = GetFurniObjects(newX, newY);

                    // Lista completa de items encontrados en cuadros afectados
                    List<RoomItem> ItemsComplete = new ArrayList<RoomItem>();
                    if (ItemsOnTile != null)
                    {
                        ItemsComplete.addAll(ItemsOnTile);
                    }
                    ItemsComplete.addAll(ItemsAffected);

                    for(RoomItem I : ItemsComplete)
                    {
                        if (I.Id == Item.Id)
                        {
                            continue; // omitirse a si mismo
                        }

                        if (!I.BaseItem.Stackable)
                        {
                            return false;
                        }

                        double altura = (I.Z+I.BaseItem.Height);
                        if (altura > newZ) // encontrar el punto mas alto
                        {
                            newZ = altura; // punto mas alto

                            if (newZ > 10.0) // es muy alto?
                            {
                                return false;
                            }
                        }
                    }
                }
                else
                {
                    // contiene usuarios el cuadro?
                    if (SquareHasUsers(newX, newY))
                    {
                        return false;
                    }

                    List<RoomItem> ItemsAffected = new ArrayList<RoomItem>();

                    // revisar cuadros afectados por el item
                    for(AffectedTile Tile : Points.values())
                    {
                        // contiene usuarios el cuadro?
                        if (SquareHasUsers(Tile.X, Tile.Y))
                        {
                            return false;
                        }

                        // encontrar items en el cuadro
                        List<RoomItem> Temp = GetFurniObjects(Tile.X, Tile.Y);
                        if (Temp != null)
                        {
                            ItemsAffected.addAll(Temp);
                        }
                    }

                    // encontrar items en el cuadro
                    List<RoomItem> ItemsOnTile = GetFurniObjects(newX, newY);

                    // Lista completa de items encontrados en cuadros afectados
                    List<RoomItem> ItemsComplete = new ArrayList<RoomItem>();
                    if (ItemsOnTile != null)
                    {
                        ItemsComplete.addAll(ItemsOnTile);
                    }
                    ItemsComplete.addAll(ItemsAffected);

                    for(RoomItem I : ItemsComplete)
                    {
                        if (I.Id == Item.Id)
                        {
                            continue; // omitirse a si mismo
                        }

                        if (!I.BaseItem.Stackable)
                        {
                            return false;
                        }

                        double altura = (I.Z+I.BaseItem.Height);
                        if (altura > newZ) // encontrar el punto mas alto
                        {
                            newZ = altura; // punto mas alto

                            if (newZ > 10.0) // es muy alto?
                            {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        List<RoomItem> container;
        Map<Integer, AffectedTile> PointList;

        if(!newItem)
        {
            container = BETA_ItemsContainer.get(BETA_ListId[Item.X][Item.Y]);
            container.remove(Item);
            GenerateSquare(Item.X, Item.Y);

            PointList = GetAffectedTiles(Item.BaseItem.Length, Item.BaseItem.Width, Item.X, Item.Y, Item.Rot);

            for(AffectedTile Tile : PointList.values())
            {
                container = BETA_ItemsContainer.get(BETA_ListId[Tile.X][Tile.Y]);
                container.remove(Item);
                GenerateSquare(Tile.X, Tile.Y);
            }
        }

        Item.X = newX;
        Item.Y = newY;
        Item.Z = newZ;
        Item.Rot = newRot;
        Item.UpdateNeeded = true;
        Item.DBNeedUpdate = true;

        if (User != null) // no Roller or wired..
        {
            Item.BaseItem.Interactor.OnPlace(User, Item);
        }

        if (newItem)
        {
            Item.IsFloorItem = true;

            if(FloorItemsBusy)
            {
                do
                {
                    try
                    {
                        sleep(100);
                    }
                    catch(Exception ex) { }
                }
                while(FloorItemsBusy);
            }
            PendingFloorItems.add(Item);

            if(Environment.Furniture.WIRED_OTHER >= Item.BaseItem.Interaction && Item.BaseItem.Interaction >= Environment.Furniture.WIRED_CONDITION)
            {
                WiredManager.RegisterWired(Item, (Item.BaseItem.Interaction-Environment.Furniture.WIRED_CONDITION)+1);
            }

            ServerMessage Message = new ServerMessage();
            Environment.InitPacket(93, Message);
            SerializeFloor(Item, Message);
            SendMessage(Message);
        }
        else
        {
            if(Environment.Furniture.WIRED_OTHER >= Item.BaseItem.Interaction && Item.BaseItem.Interaction >= Environment.Furniture.WIRED_CONDITION)
            {
                WiredManager.SetWiredPos(Item);
            }

            ServerMessage Message = new ServerMessage();
            Environment.InitPacket(95, Message);
            SerializeFloor(Item, Message);
            SendMessage(Message);
        }

        container = BETA_ItemsContainer.get(BETA_ListId[Item.X][Item.Y]);
        container.add(Item);
        GenerateSquare(Item.X, Item.Y);

        PointList = GetAffectedTiles(Item.BaseItem.Length, Item.BaseItem.Width, Item.X, Item.Y, Item.Rot);

        for(AffectedTile Tile : PointList.values())
        {
            container = BETA_ItemsContainer.get(BETA_ListId[Tile.X][Tile.Y]);
            if(!container.contains(Item))
            {
                container.add(Item);
                GenerateSquare(Tile.X, Tile.Y);
            }
        }

        int count = UserList.length;

        for (int i = 0; i < count; i++)
        {
            if (UserList[i] != null)
            {
                UpdateUserStatus(UserList[i], true, null, true);
            }
        }

        return true;
    }

    public void AddUserToRoom(Connection User_)
    {
        RoomUser User = null;

        int count = UserList.length;

        for (int i = 0; i < count; i++)
        {
            if (UserList[i] == null)
            {
                UserList[i] = User = new RoomUser(User_, Id, VirtualIdCounter++, i);
                break;
            }
        }
        if (User == null)
        {
            return;
        }

        User_.Data.RoomUser = User;
        UsersNow++;

        User.SetPos(Model.DoorX, Model.DoorY, Model.DoorZ);
        User.SetRot(Model.DoorOrientation);

        if (CheckRights(User_.Data, true))
        {
            User.isOwnerAdmin = true;
        }
        else if (CheckRights(User_.Data, false))
        {
            User.HavePowers = true;
        }

        if (User.Type == 1 && (User.Client.Data.Flags & Server.plrTeleporting) == Server.plrTeleporting)
        {
            RoomItem Item = GetFloorItem(User.Client.Data.TeleporterId);

            if (Item != null)
            {
                User.SetPos(Item.X, Item.Y, Item.Z);
                User.SetRot(Item.Rot);

                Item.ExtraData = "2";
                Item.UpdateNeeded = true;

                Item.TaskSteps = 2;
                Item.ActiveTask = 1;

                int[] xy = Item.SquareInFront();
                User.MoveTo(xy[0], xy[1], true);
            }
        }

        User.Client.Data.SetFlag(Server.plrTeleporting,false);
        User.Client.Data.TeleporterId = 0;

        ServerMessage Message = new ServerMessage();
        
        Environment.InitPacket(28, Message);

        Environment.Append(1, Message);

        Environment.Append(User.Id, Message);
        Environment.Append(User.Client.Data.UserName, Message);
        Environment.Append(User.Client.Data.Motto, Message);
        Environment.Append(User.Client.Data.Look, Message);
        Environment.Append(User.VirtualId, Message);
        Environment.Append(User.X, Message);
        Environment.Append(User.Y, Message);
        Environment.Append(Double.toString(User.Z).replace(',', '.'), Message);
        Environment.Append(User.RotBody, Message);
        Environment.Append(User.Type, Message);
        Environment.Append(User.Client.Data.Sex==1 ? "M" : "F", Message);
        Environment.Append(-1, Message);
        Environment.Append(-1, Message);
        Environment.Append(-1, Message);
        Environment.Append("", Message);
        Environment.Append(User.Client.Data.AchievementsScore, Message);

        SendMessage(Message);
        
        User.Client.OnEnterRoom(Id);

        count = UserList.length;

        for (int i = 0; i < count; i++)
        {
            if (UserList[i] != null)
            {
                if (UserList[i].Type != 1)
                {
                    UserList[i].BotAI.OnUserEnterRoom(this,UserList[i],User);
                }
            }
        }
    }

    public boolean CheckRights(Player User, boolean RequireOwnership)
    {
        if (User.IsStaff())
        {
            return true;
        }

        if (User.Id == OwnerId)
        {
            return true;
        }

        if (!RequireOwnership)
        {
            if (UsersWithRights.contains(User.Id))
            {
                return true;
            }
        }

        return false;
    }

    public void SerializeStatusUpdates(Connection Client)
    {
        if(Client != null)
        {
            List<RoomUser> Users = new ArrayList<RoomUser>();

            for (int i = 0; i < UserList.length; i++)
            {
                if (UserList[i] != null)
                {
                    Users.add(UserList[i]);
                }
            }

            int count =  Users.size();
            if(count == 0) return; // No Need Send Message

            Environment.InitPacket(34, Client.ClientMessage);
            Environment.Append(count, Client.ClientMessage);
            for(int i = 0;i<count;i++)
            {
                RoomUser User = Users.get(i);
                User.UpdateNeeded = false;
                Environment.Append(User.VirtualId, Client.ClientMessage);
                Environment.Append(User.X, Client.ClientMessage);
                Environment.Append(User.Y, Client.ClientMessage);
                Environment.Append(Double.toString(User.Z).replace(',','.'), Client.ClientMessage);
                Environment.Append(User.RotHead, Client.ClientMessage);
                Environment.Append(User.RotBody, Client.ClientMessage);
                String status = "/";
                if(User.isOwnerAdmin)
                {
                    status += "flatcrtl useradmin/";
                }
                else if(User.HavePowers)
                {
                    status += "flatcrtl /";
                }
                if(!User.Status.equals(""))
                {
                    status += User.Status;
                }
                if(!User.Status.contains("sit"))
                {
                    status += "/";
                }
                Environment.Append(status, Client.ClientMessage);
            }
            Environment.EndPacket(Client.Socket, Client.ClientMessage);
            
        }
        else
        {
            List<RoomUser> Users = new ArrayList<RoomUser>();

            for (int i = 0; i < UserList.length; i++)
            {
                if (UserList[i] != null)
                {
                    if (UserList[i].UpdateNeeded)
                    {
                        Users.add(UserList[i]);
                    }
                }
            }

            int count =  Users.size();
            if(count == 0) return; // No Need Send Message

            ServerMessage Message = new ServerMessage();
            Environment.InitPacket(34, Message);
            Environment.Append(count, Message);
            for(int i = 0;i<count;i++)
            {
                RoomUser User = Users.get(i);
                User.UpdateNeeded = false;
                Environment.Append(User.VirtualId, Message);
                Environment.Append(User.X, Message);
                Environment.Append(User.Y, Message);
                Environment.Append(Double.toString(User.Z).replace(',','.'), Message);
                Environment.Append(User.RotHead, Message);
                Environment.Append(User.RotBody, Message);
                String status = "/";
                if(User.isOwnerAdmin)
                {
                    status += "flatcrtl useradmin/";
                }
                else if(User.HavePowers)
                {
                    status += "flatcrtl /";
                }
                if(!User.Status.equals(""))
                {
                    status += User.Status;
                }
                if(!User.Status.contains("sit"))
                {
                    status += "/";
                }
                Environment.Append(status, Message);
            }
            SendMessage(Message);
        }
    }
    
    public int GetRoomPetById(int Id)
    {
        int count = UserList.length;

        for (int i = 0; i < count; i++)
        {
            if (UserList[i] != null)
            {
                if (UserList[i].Type == 2 && UserList[i].Id == Id)
                {
                    return i;
                }
            }
        }
        return -1;
    }

    public int GetRoomUserByVirtualId(int VirtualId)
    {
        int count = UserList.length;

        for (int i = 0; i < count; i++)
        {
            if (UserList[i] != null)
            {
                if (UserList[i].VirtualId == VirtualId)
                {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean UserIsBanned(int Id)
    {
        return Bans.containsKey(Id);
    }

    public void AddBan(int Id, int Minutes)
    {
        Bans.put(Id, Environment.GetTimestamp() + (Minutes*60));
    }

    public boolean HasBanExpired(int Id)
    {
        if (Environment.GetTimestamp() > Bans.get(Id))
        {
            Bans.remove(Id);
            return true;
        }

        return false;
    }

    public void SendMessage(ServerMessage Message, boolean... NeedRights)
    {
        int count = UserList.length;

        if(NeedRights.length == 0)
        {
            for (int i = 0; i < count; i++)
            {
                if (UserList[i] != null && UserList[i].Type == 1)
                {
                    Environment.EndPacket(UserList[i].Client.Socket, Message);
                }
            }
        }
        else
        {
            for (int i = 0; i < count; i++)
            {
                if (UserList[i] != null && UserList[i].Type == 1)
                {
                    if(CheckRights(UserList[i].Client.Data, false))
                    {
                        Environment.EndPacket(UserList[i].Client.Socket, Message);
                    }
                }
            }
        }
    }

    public void OnUserSay(RoomUser User, String Message)
    {
        int count = UserList.length;

        for (int i = 0; i < count; i++)
        {
            if (UserList[i] != null)
            {
                if (UserList[i].Type != 1)
                {
                    UserList[i].BotAI.OnUserSay(this, UserList[i], User, Message);
                }
            }
        }
    }

    public void ProcessEngine()
    {
        if(!Running)
        {
            Environment.RoomManager.SetActive(Running = true);
            (new Thread(this)).start();
        }
    }

    @Override
    public void run()
    {
        try
        {
            Thread.sleep(800);
        }
        catch (Exception ex)  { }

        for(RoomItem Item : FloorItems)
        {
            List<RoomItem> container = BETA_ItemsContainer.get(BETA_ListId[Item.X][Item.Y]);
            container.add(Item);
            GenerateSquare(Item.X, Item.Y);

            Map<Integer, AffectedTile> PointList = GetAffectedTiles(Item.BaseItem.Length, Item.BaseItem.Width, Item.X, Item.Y, Item.Rot);

            for(AffectedTile Tile : PointList.values())
            {
                container = BETA_ItemsContainer.get(BETA_ListId[Tile.X][Tile.Y]);
                if(!container.contains(Item))
                {
                    container.add(Item);
                    GenerateSquare(Tile.X, Tile.Y);
                }
            }
        }

        int BB_LocalPoints_R;
        int BB_LocalPoints_G;
        int BB_LocalPoints_B;
        int BB_LocalPoints_Y;
        boolean BB_ScoreNeedUpdate;
        //int[] FBALL_Points;
        boolean invalidSetStep;
        List<RoomItem> ItemsOnSquare;
        List<RoomItem> ItemsOnLastSquare;
        boolean CheckSquareItems = true;

        ServerMessage PingMessage = new ServerMessage();
        Environment.InitPacket(50, PingMessage);

        ServerMessage Message = new ServerMessage();
        
        long now1 = 0;
        while (true)
        {
            if (!Running)
            {
                break;
            }

            try
            {
                BB_LocalPoints_R = 0;
                BB_LocalPoints_G = 0;
                BB_LocalPoints_B = 0;
                BB_LocalPoints_Y = 0;
                BB_ScoreNeedUpdate = false;

                int count = UserList.length;
                for (int i = 0; i < count; i++)
                {
                    if (UserList[i] != null)
                    {
                        if (UserList[i].ActiveTask != 0)
                        {
                            if(UserList[i].TaskSteps >= 0)
                            {
                                if(--UserList[i].TaskSteps == -1)
                                {
                                    UserList[i].ProcessTask(this);
                                }
                            }
                        }

                        if (UserList[i] == null)
                        {
                            // In ProccesTask go to other room? (teleport :D)
                            continue;
                        }

                        UserList[i].IdleTime++;

                        if (UserList[i].CarryItemID > 0)
                        {
                            if (--UserList[i].CarryTimer < 1)
                            {
                                UserList[i].CarryItem(this, 0);
                            }
                        }

                        ItemsOnSquare = null;
                        invalidSetStep = false;
                        CheckSquareItems = true;

                        if (UserList[i].SetStep)
                        {
                            if (CanWalk(UserList[i], UserList[i].SetX, UserList[i].SetY, (UserList[i].GoalX == UserList[i].SetX && UserList[i].GoalY == UserList[i].SetY)))
                            {
                                if(UserList[i].Type == 1) // Only Users
                                {
                                    UserMatrix[UserList[i].X][UserList[i].Y] = false;

                                    ItemsOnLastSquare = GetFurniObjects(UserList[i].X, UserList[i].Y);

                                    UserList[i].X = UserList[i].SetX;
                                    UserList[i].Y = UserList[i].SetY;
                                    UserList[i].Z = UserList[i].SetZ;

                                    ItemsOnSquare = GetFurniObjects(UserList[i].X, UserList[i].Y);
                                    CheckSquareItems = false;

                                    UpdateUserStatus(UserList[i], false, ItemsOnSquare, false);

                                    UserMatrix[UserList[i].X][UserList[i].Y] = true;

                                    if (ItemsOnLastSquare != null)
                                    {
                                        if (ItemsOnSquare == null)
                                        {
                                            for(RoomItem Item : ItemsOnLastSquare)
                                            {
                                                WiredManager.ParseWiredVete(UserList[i].Client, UserList[i], Item);
                                            }
                                        }
                                        else
                                        {
                                            for(RoomItem Item : ItemsOnLastSquare)
                                            {
                                                if (!ItemsOnSquare.contains(Item))
                                                {
                                                    WiredManager.ParseWiredVete(UserList[i].Client, UserList[i], Item);
                                                }
                                            }

                                            for(RoomItem Item : ItemsOnSquare)
                                            {
                                                if(Item.BaseItem.Interaction != 0)
                                                {
                                                    if ((Flags & Server.rmBBanzai) == Server.rmBBanzai)
                                                    {
                                                        if (Item.BaseItem.Interaction == Environment.Furniture.BATTLEBANZAI_PATCH)
                                                        {
                                                            if (UserList[i].BattleBallType > 0)
                                                            {
                                                                if (UserList[i].BattleBallType == 1)
                                                                {
                                                                    switch (Item.BattleSteep)
                                                                    {
                                                                        case 0:
                                                                            Item.BattleSteep = 1;
                                                                            Item.ExtraData = "3";
                                                                            Item.UpdateNeeded = true;
                                                                            break;
                                                                        case 1:
                                                                            Item.BattleSteep = 2;
                                                                            Item.ExtraData = "4";
                                                                            Item.UpdateNeeded = true;
                                                                            break;
                                                                        case 2:
                                                                            BB_ScoreNeedUpdate = true;
                                                                            BB_LocalPoints_R++;
                                                                            Item.BattleSteep = -1;
                                                                            Item.ExtraData = "5";
                                                                            Item.UpdateNeeded = true;
                                                                            BB_Squares[UserList[i].X][UserList[i].Y] = 1;
                                                                            //GenSquareIntoBB(1);
                                                                            break;
                                                                    }
                                                                }
                                                                else if (UserList[i].BattleBallType == 2)
                                                                {
                                                                    switch (Item.BattleSteep)
                                                                    {
                                                                        case 0:
                                                                            Item.BattleSteep = 1;
                                                                            Item.ExtraData = "6";
                                                                            Item.UpdateNeeded = true;
                                                                            break;
                                                                        case 1:
                                                                            Item.BattleSteep = 2;
                                                                            Item.ExtraData = "7";
                                                                            Item.UpdateNeeded = true;
                                                                            break;
                                                                        case 2:
                                                                            BB_ScoreNeedUpdate = true;
                                                                            BB_LocalPoints_G++;
                                                                            Item.BattleSteep = -1;
                                                                            Item.ExtraData = "8";
                                                                            Item.UpdateNeeded = true;
                                                                            BB_Squares[UserList[i].X][UserList[i].Y] = 2;
                                                                            //GenSquareIntoBB(2);
                                                                            break;
                                                                    }

                                                                }
                                                                else if (UserList[i].BattleBallType == 3)
                                                                {
                                                                    switch (Item.BattleSteep)
                                                                    {
                                                                        case 0:
                                                                            Item.BattleSteep = 1;
                                                                            Item.ExtraData = "9";
                                                                            Item.UpdateNeeded = true;
                                                                            break;
                                                                        case 1:
                                                                            Item.BattleSteep = 2;
                                                                            Item.ExtraData = "10";
                                                                            Item.UpdateNeeded = true;
                                                                            break;
                                                                        case 2:
                                                                            BB_ScoreNeedUpdate = true;
                                                                            BB_LocalPoints_B++;
                                                                            Item.BattleSteep = -1;
                                                                            Item.ExtraData = "11";
                                                                            Item.UpdateNeeded = true;
                                                                            BB_Squares[UserList[i].X][UserList[i].Y] = 3;
                                                                            //GenSquareIntoBB(3);
                                                                            break;
                                                                    }

                                                                }
                                                                else if (UserList[i].BattleBallType == 4)
                                                                {
                                                                    switch (Item.BattleSteep)
                                                                    {
                                                                        case 0:
                                                                            Item.BattleSteep = 1;
                                                                            Item.ExtraData = "12";
                                                                            Item.UpdateNeeded = true;
                                                                            break;
                                                                        case 1:
                                                                            Item.BattleSteep = 2;
                                                                            Item.ExtraData = "13";
                                                                            Item.UpdateNeeded = true;
                                                                            break;
                                                                        case 2:
                                                                            BB_ScoreNeedUpdate = true;
                                                                            BB_LocalPoints_Y++;
                                                                            Item.BattleSteep = -1;
                                                                            Item.ExtraData = "14";
                                                                            Item.UpdateNeeded = true;
                                                                            BB_Squares[UserList[i].X][UserList[i].Y] = 4;
                                                                            //GenSquareIntoBB(4);
                                                                            break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    else if ((Flags & Server.rmFBall) == Server.rmFBall)
                                                    {
                                                        if(Environment.Furniture.BALL == Item.BaseItem.Interaction)
                                                        {
                                                            int NextX = Item.X;
                                                            int NextY = Item.Y;

                                                            switch(UserList[i].RotBody)
                                                            {
                                                                case 0:
                                                                    NextY--;
                                                                    if(!CheckSq(Item, NextX, NextY))
                                                                    {
                                                                         NextY = NextY + 2;
                                                                    }
                                                                    break;
                                                                case 1:
                                                                    NextX++;
                                                                    NextY--;
                                                                    if(!CheckSq(Item, NextX, NextY))
                                                                    {
                                                                        NextX = NextX - 2;
                                                                        NextY = NextY + 2;
                                                                    }
                                                                    break;
                                                                case 2:
                                                                    NextX++;
                                                                    if(!CheckSq(Item, NextX, NextY))
                                                                    {
                                                                        NextX = NextX - 2;
                                                                    }
                                                                    break;
                                                                case 3:
                                                                    NextX++;
                                                                    NextY++;
                                                                    if(!CheckSq(Item, NextX, NextY))
                                                                    {
                                                                        NextX = NextX - 2;
                                                                        NextY = NextY - 2;
                                                                    }
                                                                    break;
                                                                case 4:
                                                                    NextY++;
                                                                    if(!CheckSq(Item, NextX, NextY))
                                                                    {
                                                                        NextY = NextY - 2;
                                                                    }
                                                                    break;
                                                                case 5:
                                                                    NextX--;
                                                                    NextY++;
                                                                    if(!CheckSq(Item, NextX, NextY))
                                                                    {
                                                                        NextX = NextX + 2;
                                                                        NextY = NextY - 2;
                                                                    }
                                                                    break;
                                                                case 6:
                                                                    NextX--;
                                                                    if(!CheckSq(Item, NextX, NextY))
                                                                    {
                                                                        NextX = NextX + 2;
                                                                    }
                                                                    break;
                                                                case 7:
                                                                    NextX--;
                                                                    NextY--;
                                                                    if(!CheckSq(Item, NextX, NextY))
                                                                    {
                                                                        NextX = NextX + 2;
                                                                        NextY = NextY + 2;
                                                                    }
                                                                    break;
                                                            }
                                                            if(Item.BaseItem.ItemName.contains("fball_ball"))
                                                            {
                                                                Item.ExtraData = "11";
                                                            }
                                                            else if(Item.BaseItem.ItemName.equals("bb_puck"))
                                                            {
                                                                Item.ExtraData = UserList[i].BattleBallType + "";
                                                            }
                                                            this.SetFloorItem(null, Item, NextX, NextY, 2, false);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                else // Pets - Bots
                                {
                                    UserMatrix[UserList[i].X][UserList[i].Y] = false;

                                    UserList[i].X = UserList[i].SetX;
                                    UserList[i].Y = UserList[i].SetY;
                                    UserList[i].Z = UserList[i].SetZ;

                                    ItemsOnSquare = GetFurniObjects(UserList[i].X, UserList[i].Y);
                                    CheckSquareItems = false;

                                    UpdateUserStatus(UserList[i], false, ItemsOnSquare, false);

                                    UserMatrix[UserList[i].X][UserList[i].Y] = true;
                                }
                            }
                            else
                            {
                                invalidSetStep = true;
                            }

                            UserList[i].SetStep = false;
                        }

                        if (UserList[i].PathRecalcNeeded)
                        {
                            UserList[i].GoalX = UserList[i].PathRecalcX;
                            UserList[i].GoalY = UserList[i].PathRecalcY;

                            UserList[i].Path.clear();
                            UserList[i].Path = FindPath(UserList[i]);
                            if (UserList[i].Path != null)
                            {
                                if (UserList[i].Path.size() > 1)
                                {
                                    UserList[i].PathStep = 1;
                                    UserList[i].IsWalking = true;
                                    UserList[i].PathRecalcNeeded = false;
                                }
                                else
                                {
                                    UserList[i].PathRecalcNeeded = false;
                                    UserList[i].Path.clear();
                                }
                            }
                            else
                            {
                                UserList[i].PathRecalcNeeded = false;
                                UserList[i].ClearMovement(true);
                            }
                        }

                        if (UserList[i].IsWalking)
                        {
                            if (invalidSetStep || UserList[i].PathStep >= UserList[i].Path.size() || UserList[i].GoalX == UserList[i].X && UserList[i].Y == UserList[i].GoalY)
                            {
                                UserList[i].Path.clear();
                                UserList[i].IsWalking = false;
                                if(UserList[i].HaveStatus("mv"))
                                {
                                    UserList[i].RemoveStatus();
                                }
                                UserList[i].PathRecalcNeeded = false;

                                if (UserList[i].X == Model.DoorX && UserList[i].Y == Model.DoorY)
                                {
                                    RemoveUserFromRoom(UserList[i].Client, true, false, false);
                                    continue;
                                }

                                if (CheckSquareItems)
                                {
                                    ItemsOnSquare = GetFurniObjects(UserList[i].X, UserList[i].Y);
                                }

                                UpdateUserStatus(UserList[i], false, ItemsOnSquare, true);

                                if (ItemsOnSquare != null)
                                {
                                    for(RoomItem Item : ItemsOnSquare)
                                    {
                                        if (Item.BaseItem.Interaction == Environment.Furniture.ROLLER)
                                        {
                                            /*Thread Roller = new Thread(delegate() { RollerProcess(Item, Lista[i]); });
                                            Roller.Priority = ThreadPriority.Lowest;
                                            Roller.Start();*/
                                        }
                                    }
                                }
                            }
                            else
                            {
                                int k = (UserList[i].Path.size() - UserList[i].PathStep) - 1;
                                Coord NextStep = UserList[i].Path.get(k);
                                UserList[i].PathStep++;

                                int nextX = NextStep.x;
                                int nextY = NextStep.y;

                                if(UserList[i].HaveStatus("mv"))
                                {
                                    UserList[i].RemoveStatus();
                                }

                                if (CanWalk(UserList[i], nextX, nextY, (UserList[i].GoalX == nextX && UserList[i].GoalY == nextY)))
                                {
                                    double nextZ = SqAbsoluteHeight(nextX, nextY, true, null);

                                    UserList[i].SetStatus("mv", nextX + "," + nextY + "," + Double.toString(nextZ).replace(',', '.'));

                                    int newRot = Rotation.Calculate(UserList[i].X, UserList[i].Y, nextX, nextY);

                                    UserList[i].RotBody = newRot;
                                    UserList[i].RotHead = newRot;

                                    UserList[i].SetStep = true;

                                    if(nextX != BedMatrix[nextX][nextY].x || nextY != BedMatrix[nextX][nextY].y)
                                    {
                                        UserList[i].GoalX = BedMatrix[nextX][nextY].x;
                                        UserList[i].GoalY = BedMatrix[nextX][nextY].y;
                                    }

                                    UserList[i].SetX = BedMatrix[nextX][nextY].x;
                                    UserList[i].SetY = BedMatrix[nextX][nextY].y;
                                    UserList[i].SetZ = nextZ;

                                    if(UserList[i].Type == 1) // Only Users
                                    {
                                        ItemsOnSquare = GetFurniObjects(UserList[i].SetX, UserList[i].SetY);

                                        if(UserList[i].BattleBallType==0) // keep alive battle banzai
                                        {
                                            UserList[i].EffectStatus = 0;
                                        }

                                        if (ItemsOnSquare != null)
                                        {
                                            for(RoomItem Item : ItemsOnSquare)
                                            {
                                                if(Item.BaseItem.Interaction == Environment.Furniture.SKATES)
                                                {
                                                    if (UserList[i].CurrentEffect != 54+UserList[i].Client.Data.Sex)
                                                    {
                                                        UserList[i].EffectStatus = 1;
                                                        UserList[i].IsBuyEffect = false;
                                                        UserList[i].CurrentEffect = 54+UserList[i].Client.Data.Sex;
                                                    }
                                                    else
                                                    {
                                                        UserList[i].EffectStatus = 2; // keep alive
                                                    }
                                                }
                                                else if(Environment.Furniture.BATTLEBANZAI_GATEY >= Item.BaseItem.Interaction && Item.BaseItem.Interaction >= Environment.Furniture.BATTLEBANZAI_GATER)
                                                {
                                                    if(UserList[i].BattleBallType != 0)
                                                    {
                                                        UserList[i].EffectStatus = 0;
                                                        UserList[i].BattleBallType = 0;
                                                    }
                                                    else
                                                    {
                                                        UserList[i].EffectStatus = 1;
                                                        UserList[i].IsBuyEffect = false;
                                                        UserList[i].BattleBallType = Item.BaseItem.Interaction - 7;
                                                        UserList[i].CurrentEffect = UserList[i].BattleBallType + 32;
                                                    }
                                                }
                                                else if(Item.BaseItem.Interaction >= Environment.Furniture.WATER1 && Environment.Furniture.WATER3 >= Item.BaseItem.Interaction)
                                                {
                                                    if (Item.BaseItem.Interaction == Environment.Furniture.WATER1)
                                                    {
                                                        if (UserList[i].CurrentEffect != 30)
                                                        {
                                                            UserList[i].EffectStatus = 1;
                                                            UserList[i].IsBuyEffect = false;
                                                            UserList[i].CurrentEffect = 30;
                                                        }
                                                        else
                                                        {
                                                            UserList[i].EffectStatus = 2; // keep alive
                                                        }
                                                    }
                                                    else if (Item.BaseItem.Interaction == Environment.Furniture.WATER2)
                                                    {
                                                        if (UserList[i].CurrentEffect != 29)
                                                        {
                                                            UserList[i].EffectStatus = 1;
                                                            UserList[i].IsBuyEffect = false;
                                                            UserList[i].CurrentEffect = 29;
                                                        }
                                                        else
                                                        {
                                                            UserList[i].EffectStatus = 2; // keep alive
                                                        }
                                                    }
                                                    else if (Item.BaseItem.Interaction == Environment.Furniture.WATER3)
                                                    {
                                                        if (UserList[i].CurrentEffect != 37)
                                                        {
                                                            UserList[i].EffectStatus = 1;
                                                            UserList[i].IsBuyEffect = false;
                                                            UserList[i].CurrentEffect = 37;
                                                        }
                                                        else
                                                        {
                                                            UserList[i].EffectStatus = 2; // keep alive
                                                        }
                                                    }
                                                }
                                                WiredManager.ParseWiredWalksOnFurni(UserList[i].Client, Item);
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    UserList[i].IsWalking = false;
                                }
                            }

                            UserList[i].UpdateNeeded = true;
                        }
                        else
                        {
                            if (UserList[i].HaveStatus("mv"))
                            {
                                UserList[i].RemoveStatus();
                            }
                        }

                        if (UserList[i].Type != 1)
                        {
                            UserList[i].BotAI.OnTimerTick(this, UserList[i]);
                        }
                    }
                }

                long now2 = System.currentTimeMillis();
                if(now1 > now2)
                {
                    try
                    {
                        Thread.sleep(now1 - now2);
                    }
                    catch (Exception ex) { }
                }
                else if(now1 > 0)
                {
                    System.err.println("El threadroom se ejecuto en mas de 500 ms: " + (now2 - now1));
                }
                now1 = System.currentTimeMillis() + 500;

                SerializeStatusUpdates(null);

                for (int i = 0; i < UserList.length; i++)
                {
                    if (UserList[i] != null && UserList[i].Type == 1)
                    {
                        if(UserList[i].CurrentEffect > 0)
                        {
                            if(UserList[i].EffectStatus != 0)
                            {
                                if(UserList[i].EffectStatus == 1) // Start efect
                                {
                                    UserList[i].EffectStatus = 2; // keep alive

                                    Environment.InitPacket(485, Message);
                                    Environment.Append(UserList[i].VirtualId, Message);
                                    Environment.Append(UserList[i].CurrentEffect, Message);
                                    SendMessage(Message);
                                }
                            }
                            else
                            {
                                if(!UserList[i].IsBuyEffect)
                                {
                                    UserList[i].CurrentEffect = 0;

                                    Environment.InitPacket(485, Message);
                                    Environment.Append(UserList[i].VirtualId, Message);
                                    Environment.Append(-1, Message);
                                    SendMessage(Message);
                                }
                            }
                        }

                        if(UserList[i].IdleTime < 600)
                        {
                            if(UserList[i].IsAsleep)
                            {
                                UserList[i].IsAsleep = false;

                                Environment.InitPacket(486, Message);
                                Environment.Append(UserList[i].VirtualId, Message);
                                Environment.Append(UserList[i].IsAsleep, Message);
                                SendMessage(Message);
                            }
                        }
                        else if(!UserList[i].IsAsleep)
                        {
                            UserList[i].IsAsleep = true;

                            Environment.InitPacket(486, Message);
                            Environment.Append(UserList[i].VirtualId, Message);
                            Environment.Append(UserList[i].IsAsleep, Message);
                            SendMessage(Message);
                        }

                        if(++UserList[i].Ping_Steep == 40)
                        {
                            if ((UserList[i].Client.Data.Flags & Server.plrPongOk) == Server.plrPongOk)
                            {
                                Environment.EndPacket(UserList[i].Client.Socket, PingMessage);
                                UserList[i].Client.Data.SetFlag(Server.plrPongOk,false);
                            }
                            else
                            {
                                UserList[i].Client.Disconnect();
                            }
                        }
                    }
                }

                for(RoomItem Item : FloorItems)
                {
                    if (Item.ActiveTask != 0)
                    {
                        if(Item.TaskSteps >= 0)
                        {
                            if(--Item.TaskSteps == -1)
                            {
                                Item.ProcessTask(this);
                            }
                        }
                    }

                    if(BB_ScoreNeedUpdate)
                    {
                        if(Item.BaseItem.Interaction == Environment.Furniture.BATTLEBANZAI_SCORE_R)
                        {
                            if(BB_LocalPoints_R > 0)
                            {
                                BB_Points_R += BB_LocalPoints_R;
                                Item.ExtraData = Integer.toString(BB_Points_R);
                                Item.UpdateNeeded = true;
                            }
                        }
                        else if(Item.BaseItem.Interaction == Environment.Furniture.BATTLEBANZAI_SCORE_G)
                        {
                            if(BB_LocalPoints_G > 0)
                            {
                                BB_Points_G += BB_LocalPoints_G;
                                Item.ExtraData = Integer.toString(BB_Points_G);
                                Item.UpdateNeeded = true;
                            }
                        }
                        else if(Item.BaseItem.Interaction == Environment.Furniture.BATTLEBANZAI_SCORE_B)
                        {
                            if(BB_LocalPoints_B > 0)
                            {
                                BB_Points_B += BB_LocalPoints_B;
                                Item.ExtraData = Integer.toString(BB_Points_B);
                                Item.UpdateNeeded = true;
                            }
                        }
                        else if(Item.BaseItem.Interaction == Environment.Furniture.BATTLEBANZAI_SCORE_Y)
                        {
                            if(BB_LocalPoints_Y > 0)
                            {
                                BB_Points_Y += BB_LocalPoints_Y;
                                Item.ExtraData = Integer.toString(BB_Points_Y);
                                Item.UpdateNeeded = true;
                            }
                        }
                    }

                    if (!Item.UpdateNeeded)
                    {
                        continue;
                    }
                    
                    Item.UpdateState(this);
                }

                if(!PendingFloorItems.isEmpty())
                {
                    FloorItemsBusy = true;
                    FloorItems.addAll(PendingFloorItems);
                    PendingFloorItems.clear();
                    FloorItemsBusy = false;
                }
            }
            catch (Exception ex)
            {
                Environment.Log.Print(ex);
            }
        }

        for(int a = 0;a<Model.MapSizeX;a++)
        {
            for(int b = 0;b<Model.MapSizeY;b++)
            {
                List<RoomItem> tmp = BETA_ItemsContainer.get(BETA_ListId[a][b]);
                tmp.clear();
            }
        }

        Environment.RoomManager.SetActive(false);

        DatabaseClient DB;
        try
        {
            DB = new DatabaseClient(Environment.DataBase);
        }
        catch (Exception ex)
        {
            Environment.Log.Print(ex);
            return;
        }

        try
        {
            if ((Flags & Server.rmIsNew) == Server.rmIsNew)
            {
                String RoomTags = "";

                for(String Tag : Tags)
                {
                    RoomTags += Environment.b64Encode(Tag.length()) + Tag;
                }

                DB.SecureExec(
                        "INSERT INTO `rooms` (`id` ,`caption` ,`owner` ,`owner_id` ,`description` ,`category` ,`state` ,`users_max` ,`model_name` ,`score` ,`tags` ,`icon_bg` ,`icon_fg` ,`icon_items` ,`password` ,`wallpaper` ,`floor` ,`landscape` ,`allow_pets` ,`allow_pets_eat` ,`allow_walkthrough` ,`hide_wall` ,`wall_anchor` ,`floor_anchor` ,`SttaffPickUp`)VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
                        Integer.toString(Id),
                        Name,
                        Owner,
                        Integer.toString(OwnerId),
                        Description,
                        Integer.toString(Category),
                        Integer.toString(State),
                        Integer.toString(UsersMax),
                        ModelName,
                        Integer.toString(Score),
                        RoomTags,
                        Integer.toString(Icon.BackgroundImage),
                        Integer.toString(Icon.ForegroundImage),
                        "",
                        Password,
                        Wallpaper,
                        Floor,
                        Landscape,
                        ((Flags & Server.rmAllowPets) == Server.rmAllowPets ? "1" : "0"),
                        ((Flags & Server.rmAllowPetsEat) == Server.rmAllowPetsEat ? "1" : "0"),
                        ((Flags & Server.rmAllowWalkthrough) == Server.rmAllowWalkthrough ? "1" : "0"),
                        ((Flags & Server.rmHideWall) == Server.rmHideWall ? "1" : "0"),
                        Integer.toString(WallAnchor),
                        Integer.toString(FloorAnchor),
                        ((Flags & Server.rmStaffPickUp) == Server.rmStaffPickUp ? "1" : "0"));

                SetFlag(Server.rmIsNew,false);
            }
            else
            {
                String RoomTags = "";

                for(String Tag : Tags)
                {
                    RoomTags += Environment.b64Encode(Tag.length()) + Tag;
                }

                DB.SecureExec("UPDATE `rooms` SET "
                        + "`caption` =  ? , "
                        + "`description` =  ? , "
                        + "`category` =  '"+Category+"' , "
                        + "`state` =  '"+State+"' , "
                        + "`users_max` =  '"+UsersMax+"' , "
                        + "`score` =  '"+Score+"' , "
                        + "`tags` =  ? , "
                        + "`icon_bg` =  '"+Icon.BackgroundImage+"' , "
                        + "`icon_fg` =  '"+Icon.ForegroundImage+"' , "
                        + "`password` =  ? , "
                        + "`wallpaper` =  '"+Wallpaper+"' , "
                        + "`floor` =  '"+Floor+"' , "
                        + "`landscape` =  '"+Landscape+"' , "
                        + "`allow_pets` =  '"+((Flags & Server.rmAllowPets) == Server.rmAllowPets ? 1 : 0)+"' , "
                        + "`allow_pets_eat` =  '"+((Flags & Server.rmAllowPetsEat) == Server.rmAllowPetsEat ? 1 : 0)+"' , "
                        + "`allow_walkthrough` =  '"+((Flags & Server.rmAllowWalkthrough) == Server.rmAllowWalkthrough ? 1 : 0)+"' , "
                        + "`hide_wall` =  '"+((Flags & Server.rmHideWall) == Server.rmHideWall ? 1 : 0)+"' , "
                        + "`wall_anchor` =  '"+WallAnchor+"' , "
                        + "`floor_anchor` =  '"+FloorAnchor+"' , "
                        + "`SttaffPickUp` =  '"+((Flags & Server.rmStaffPickUp) == Server.rmStaffPickUp ? 1 : 0)+"' "
                        + "WHERE `id` = '"+Id+"' LIMIT 1;",
                        Name,
                        Description,
                        RoomTags,
                        Password);
            }


            for(RoomItem Item : FloorItems)
            {
                if(!Item.DBNeedUpdate) continue;

                String InfoData = "";
                
                DB.SecureExec("UPDATE items SET `room_id`='"+Id+"',`extra_data`=?,`x`='"+Item.X+"',`y`="+Item.Y+",`z`="+Item.Z+",`rot`='"+Item.Rot+"',`info_data`=? WHERE id = "+Item.Id+";",
                        Item.ExtraData,
                        InfoData
                        );
            }

            for(RoomItem Item : WallItems)
            {
                if(!Item.DBNeedUpdate) continue;

                DB.SecureExec("UPDATE items SET `room_id`='"+Id+"',`extra_data`=?,`x`='"+Item.X+"',`y`="+Item.Y+",`z`="+Item.Z+",`rot`='"+Item.Rot+"',`wall_pos`=? WHERE id = "+Item.Id+";",
                            Item.ExtraData,
                            Item.WallPos
                            );
            }
        }
        catch(Exception ex)
        {
            Environment.Log.Print(ex);
        }
        DB.Close();
    }

    public void LoadItems()
    {
        DatabaseClient DB;
        try
        {
            DB = new DatabaseClient(Environment.DataBase);
        }
        catch (Exception ex)
        {
            Environment.Log.Print(ex);
            return;
        }

        try
        {
            ResultSet table = DB.Query("SELECT * FROM items WHERE room_id = "+Id+";");

            while (table.next())
            {
                RoomItem RoomItem = new RoomItem(Environment);
                RoomItem.Id = table.getInt("id");
                RoomItem.RoomId = Id;
                RoomItem.BaseItem = Environment.Furniture.Items.get(table.getInt("base_item"));
                RoomItem.ExtraData = table.getString("extra_data");
                RoomItem.X = table.getInt("x");
                RoomItem.Y = table.getInt("y");
                RoomItem.Z = table.getDouble("z");
                RoomItem.Rot = table.getInt("rot");
                RoomItem.WallPos = table.getString("wall_pos");

                if(RoomItem.BaseItem.Interaction == Environment.Furniture.TELEPORT)
                {
                    Environment.Teleports.SetRoom(RoomItem.Id, Id);
                }

                if(RoomItem.BaseItem.Type.equals("s"))
                {
                    RoomItem.IsFloorItem = true;
                }
                else if(RoomItem.BaseItem.Type.equals("i"))
                {
                    RoomItem.IsWallItem = true;
                }
                FloorItems.add(RoomItem);
            }
        }
        catch (Exception ex)
        {
            Environment.Log.Print(ex);
        }
        DB.Close();
    }

    public void RemoveUserFromRoom(Connection Client, boolean NotifyClient, boolean NotifyKick, boolean NoDestroy)
    {
        ServerMessage Message = new ServerMessage();
        
        if (NotifyClient)
        {
            if (NotifyKick)
            {
                Environment.InitPacket(33, Message);
                Environment.Append(4008, Message);
                Environment.EndPacket(Client.Socket, Message);
            }
            Environment.InitPacket(18, Message);
            Environment.EndPacket(Client.Socket, Message);
        }

        // Testing exception
        int test = Client.Data.RoomUser.X;
        int test2 = Client.Data.RoomUser.Y;
        boolean test3 = UserMatrix[Client.Data.RoomUser.X][Client.Data.RoomUser.Y] = false;

        UserMatrix[Client.Data.RoomUser.X][Client.Data.RoomUser.Y] = false;

        Environment.InitPacket(29, Message);
        Environment.Append(Integer.toString(Client.Data.RoomUser.VirtualId), Message);
        UserList[Client.Data.RoomUser.UserIndex] = null;
        Client.Data.RoomUser = null;
        UsersNow--;
        SendMessage(Message);

        if (Client.Data.Trade != null)
        {
            Client.Data.Trade.CloseTrade();
        }

        if (CheckRights(Client.Data, true))
        {
            if (Event != null)
            {
                Event = null;

                Environment.InitPacket(370, Message);
                Environment.Append("-1", Message);
                SendMessage(Message);
            }
        }

        Client.OnLeaveRoom();

        int count = UserList.length;

        for (int i = 0; i < count; i++)
        {
            if (UserList[i] != null)
            {
                if (UserList[i].Type != 1)
                {
                    UserList[i].BotAI.OnUserLeaveRoom(this, UserList[i], Client);

                    if (UserList[i].Type == 2 && UserList[i].PetData.OwnerId == Client.Data.Id && !CheckRights(Client.Data, true))
                    {                        
                        Client.Data.InventoryPets.put(UserList[i].Id, UserList[i].PetData);

                        Environment.InitPacket(603, Message);
                        UserList[i].PetData.SerializeInventory(Environment, Message);
                        Environment.EndPacket(Client.Socket, Message);

                        RemovePet(i, false);
                    }
                }
            }
        }

        if(UsersNow == 0 && !NoDestroy)
        {
            StopRunning();

            Player pOwner = Environment.ClientManager.GetClient(OwnerId);
            if(pOwner != null)
            {
                if((pOwner.Flags & Server.plrOnline) == Server.plrOnline)
                {
                    return; // No delete data if owner is online
                }
            }

            try
            {
                Environment.RoomManager.UnloadRoom(Id);
            }
            catch (Exception ex)
            {
                Environment.Log.Print(ex);
            }

            try
            {
                finalize();
            }
            catch (Throwable ex)
            {
                Environment.Log.Print(ex);
            }
        }
    }

    public boolean CanWalk(RoomUser User, int X, int Y, boolean LastStep)
    {
        if (!ValidTile(X, Y))
        {
            return false;
        }

        if (User.AllowOverride)
        {
            return true;
        }

        if (Matrix[X][Y] == MatrixState.BLOCKED)
        {
            return false;
        }
        else if (!LastStep && Matrix[X][Y] != MatrixState.WALKABLE)
        {
            return false;
        }

        if ((Flags & Server.rmAllowWalkthrough) != Server.rmAllowWalkthrough && SquareHasUsers(X, Y))
        {
            return false;
        }

        return true;
    }

    public boolean CanWalk2(int X, int Y, boolean LastStep)
    {
        if (!ValidTile(X, Y))
        {
            return false;
        }

        if (Matrix[X][Y] != MatrixState.WALKABLE)
        {
            return false;
        }

        if (SquareHasUsers(X, Y))
        {
            return false;
        }

        return true;
    }

    private Map<Integer, AffectedTile> GetAffectedTiles(int Length, int Width, int PosX, int PosY, int Rotation)
    {
        int x = 0;

        Map<Integer, AffectedTile> PointList = new HashMap<Integer, AffectedTile>();

        if (Length > 1)
        {
            if (Rotation == 0 || Rotation == 4)
            {
                for (int i = 1; i < Length; i++)
                {
                    PointList.put(x++, new AffectedTile(PosX, PosY + i, i));

                    for (int j = 1; j < Width; j++)
                    {
                        PointList.put(x++, new AffectedTile(PosX + j, PosY + i, (i < j) ? j : i));
                    }
                }
            }
            else if (Rotation == 2 || Rotation == 6)
            {
                for (int i = 1; i < Length; i++)
                {
                    PointList.put(x++, new AffectedTile(PosX + i, PosY, i));

                    for (int j = 1; j < Width; j++)
                    {
                        PointList.put(x++, new AffectedTile(PosX + i, PosY + j, (i < j) ? j : i));
                    }
                }
            }
        }

        if (Width > 1)
        {
            if (Rotation == 0 || Rotation == 4)
            {
                for (int i = 1; i < Width; i++)
                {
                    PointList.put(x++, new AffectedTile(PosX + i, PosY, i));

                    for (int j = 1; j < Length; j++)
                    {
                        PointList.put(x++, new AffectedTile(PosX + i, PosY + j, (i < j) ? j : i));
                    }
                }
            }
            else if (Rotation == 2 || Rotation == 6)
            {
                for (int i = 1; i < Width; i++)
                {
                    PointList.put(x++, new AffectedTile(PosX, PosY + i, i));

                    for (int j = 1; j < Length; j++)
                    {
                        PointList.put(x++, new AffectedTile(PosX + j, PosY + i, (i < j) ? j : i));
                    }
                }
            }
        }

        return PointList;
    }

    private List<RoomItem> GetFurniObjects(int X, int Y)
    {
        try
        {
            return BETA_ItemsContainer.get(BETA_ListId[X][Y]);
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public double SqAbsoluteHeight(int X, int Y, boolean CheckSquareItems, List<RoomItem> ItemsOnSquare)
    {
        double HighestStack = 0;

        if (CheckSquareItems)
        {
            ItemsOnSquare = GetFurniObjects(X, Y);
        }
        if (ItemsOnSquare != null)
        {
            for(RoomItem Item : ItemsOnSquare)
            {
                if ((Item.Z + Item.BaseItem.Height) > HighestStack)
                {
                    HighestStack = (Item.Z + Item.BaseItem.Height);
                }
            }
        }

        double floorHeight = Model.SqFloorHeight[X][Y];
        double stackHeight = HighestStack - Model.SqFloorHeight[X][Y];

        if (stackHeight < 0)
        {
            stackHeight = 0;
        }

        return (floorHeight + stackHeight);
    }

    public void UpdateUserStatus(RoomUser User, boolean CheckSquareItems, List<RoomItem> ItemsOnSquare, boolean check)
    {
        double newZ = SqAbsoluteHeight(User.X, User.Y, CheckSquareItems, ItemsOnSquare);

        if (newZ != User.Z)
        {
            User.Z = newZ;
            User.UpdateNeeded = true;
        }

        if (check)
        {
            switch (Matrix[User.X][User.Y])
            {
                case SENTAR:
                    if (!User.HaveStatus("sit"))
                    {
                        User.SetStatus("sit", "1.1");
                    }

                    User.Z = newZ;
                    User.RotHead = MatrixRot[User.X][User.Y];
                    User.RotBody = MatrixRot[User.X][User.Y];
                    User.UpdateNeeded = true;
                    break;

                case ACOSTAR:
                    if(!User.HaveStatus("lay"))
                    {
                        User.SetStatus("lay", "1.45");// Habbo.es sucks xD // new height from habbo ES #241/@bIHQBI0.0{2}HH/lay 1.9 null/{2}{1}
                    }

                    User.Z = newZ;
                    User.RotHead = MatrixRot[User.X][User.Y];
                    User.RotBody = MatrixRot[User.X][User.Y];
                    User.UpdateNeeded = true;
                    break;

                default:
                    if (User.HaveStatus("sit"))
                    {
                        User.RemoveStatus();
                    }
                    else if(User.HaveStatus("lay"))
                    {
                        User.RemoveStatus();
                    }
                break;
            }
        }
    }


    public RoomUser DeployPet(RoomBot Bot, Pet PetData, int X, int Y, Double Z)
    {
        RoomUser User = null;

        int count = UserList.length;

        for (int i = 0; i < count; i++)
        {
            if (UserList[i] == null)
            {
                UserList[i] = User = new RoomUser(null, Id, VirtualIdCounter++, i);
                break;
            }
        }
        if (User == null)
        {
            return null;
        }
        User.Type = 2;
        User.Id = PetData.Id;
        PetCounter++;

        // Verify tiles are valid
        if (!ValidTile(X, Y))
        {
            X = Model.DoorX;
            Y = Model.DoorY;
            Z = Model.DoorZ;
        }

        User.SetPos(X, Y, Z);
        User.SetRot(Model.DoorOrientation);

        UserMatrix[X][Y] = true;

        User.BotData = Bot;
        User.BotAI = new PetBot(PetData.Type);
        User.PetData = PetData;

        UpdateUserStatus(User,true,null,false);
        User.UpdateNeeded = true;

        ServerMessage EnterMessage = new ServerMessage();
        Environment.InitPacket(28, EnterMessage);
        Environment.Append(1, EnterMessage);
        Environment.Append(User.Id, EnterMessage);
        Environment.Append(User.BotData.Name, EnterMessage);
        Environment.Append(User.BotData.Motto, EnterMessage);
        Environment.Append(User.BotData.Look, EnterMessage);
        Environment.Append(User.VirtualId, EnterMessage);
        Environment.Append(User.X, EnterMessage);
        Environment.Append(User.Y, EnterMessage);
        Environment.Append(Double.toString(User.Z).replace(',', '.'), EnterMessage);
        Environment.Append(User.RotBody, EnterMessage);
        Environment.Append(User.Type, EnterMessage);
        Environment.Append(User.PetData.Race, EnterMessage);
        SendMessage(EnterMessage);

        User.BotAI.OnSelfEnterRoom(this, User);

        return User;
    }

    public void RemovePet(int PetId, boolean Kicked)
    {
        UserList[PetId].BotAI.OnSelfLeaveRoom(this, UserList[PetId], Kicked);

        UserMatrix[UserList[PetId].X][UserList[PetId].Y] = false;

        ServerMessage LeaveMessage = new ServerMessage();
        Environment.InitPacket(29, LeaveMessage);
        Environment.Append(Integer.toString(UserList[PetId].VirtualId), LeaveMessage);
        SendMessage(LeaveMessage);

        UserList[PetId] = null;
        PetCounter--;
    }


    public boolean SquareHasUsers(int X, int Y)
    {
        Coord Coord = BedMatrix[X][Y];
        return UserMatrix[Coord.x][Coord.y];
    }

    private List<Point> ValidMoves(RoomUser User, int x, int y, Point[] Movements)
    {
        List<Point> ValidMoves = new ArrayList<Point>();
        if(User.Type == 1)
        {
            for(int i = 0;i<Movements.length;i++)
            {
                Point movePoint = Movements[i];

                int newX = x + movePoint.x;
                int newY = y + movePoint.y;

                if (IsSquareOpen(User, newX, newY))
                {
                    ValidMoves.add(new Point(newX, newY));
                }
            }
        }
        else
        {
            for(int i = 0;i<Movements.length;i++)
            {
                Point movePoint = Movements[i];

                int newX = x + movePoint.x;
                int newY = y + movePoint.y;

                if (!IsSquareOpen(User, newX, newY)) continue;

                if (movePoint.x == -1)
                {
                    switch (movePoint.y)
                    {
                        case 1:
                            if (!IsSquareOpen(User, newX + 1, newY) || !IsSquareOpen(User, newX, newY - 1))
                            {
                                continue;
                            }
                            break;
                        case -1:
                            if (!IsSquareOpen(User, newX + 1, newY) || !IsSquareOpen(User, newX, newY + 1))
                            {
                                continue;
                            }
                            break;
                    }
                }
                else if (movePoint.x == 1)
                {
                    switch (movePoint.y)
                    {
                        case 1:
                            if (!IsSquareOpen(User, newX - 1, newY) || !IsSquareOpen(User, newX, newY - 1))
                            {
                                continue;
                            }
                            break;
                        case -1:
                            if (!IsSquareOpen(User, newX - 1, newY) || !IsSquareOpen(User, newX, newY + 1))
                            {
                                continue;
                            }
                            break;
                    }
                }

                ValidMoves.add(new Point(newX, newY));
            }
        }
        return ValidMoves;
    }

    private boolean ValidTile(int X, int Y)
    {
        if (X < 1 || Y < 1 || X >= Model.MapSizeX || Y >= Model.MapSizeY)
        {
            return false;
        }
        return true;
    }

    public void ONSTARBB()
    {
       for(RoomItem Item: FloorItems)
       {
           if(Item.BaseItem.ItemName.contains("bb_apparatus"))
           {
               Item.ExtraData = "1";
               Item.UpdateState(this);
           }
       }
    }
    public void ONENDBB()
    {
       for(RoomItem Item: FloorItems)
       {
           if(Item.BaseItem.ItemName.contains("bb_apparatus"))
           {
               Item.ExtraData = "0";
               Item.UpdateState(this);
           }
       }
    }
    private boolean IsSquareOpen(RoomUser User, int x, int y)
    {
        if (User.AllowOverride)
        {
            if(ValidTile(x, y))
            {
                return true;
            }
            return false;
        }

        if (User.X == x && User.Y == y)
        {
            return true;
        }

        if (!CanWalk(User, x, y, (User.GoalX == x && User.GoalY == y)))
        {
            return false;
        }

        return true;
    }


    public List<Coord> FindPath(RoomUser User)
    {
        int goalX = User.GoalX;
        int goalY = User.GoalY;

        if (goalX == -1 || goalY == -1)
        {
            return null;
        }

        if (!User.AllowOverride && User.CheckSquare)
        {
            if (!CanWalk(User, goalX, goalY, true))
            {
                return null;
            }
        }

        int[][] DistanceSteps = new int[Model.MapSizeX][Model.MapSizeY];

        for (int x = 0; x < Model.MapSizeX; x++)
        {
            for (int y = 0; y < Model.MapSizeY; y++)
            {
                DistanceSteps[x][y] = 100;
            }
        }

        int UserX = User.X;
        int UserY = User.Y;

        DistanceSteps[User.X][User.Y] = 0;

        Point[] Movements;

        if (User.Type != 1)
        {
            Movements = new Point[]
            {
                new Point(0, -1),
                new Point(1, 0),
                new Point(0, 1),
                new Point(-1, 0)
            };
        }
        else
        {
            Movements = new Point[]
            {
                new Point(0, -1),
                new Point(1, 0),
                new Point(0, 1),
                new Point(-1, 0),
                new Point(-1, -1),
                new Point(1, -1),
                new Point(1, 1),
                new Point(-1, 1)
            };
        }

        for(int i = 0;Model.Area > i;i++)
        {
            boolean MadeProgress = false;

            for(Point MainPoint : Squares)
            {
                int x = MainPoint.x;
                int y = MainPoint.y;

                if (IsSquareOpen(User, x, y))
                {
                    int passHere = DistanceSteps[x][y];

                    List<Point> ValidMoves = ValidMoves(User, x, y, Movements);

                    for(Point movePoint : ValidMoves)
                    {
                        int newX = movePoint.x;
                        int newY = movePoint.y;
                        int newPass = passHere + 1;

                        if (DistanceSteps[newX][newY] > newPass)
                        {
                            DistanceSteps[newX][newY] = newPass;
                            MadeProgress = true;
                        }
                    }
                }
            }
            if (!MadeProgress)
            {
                break;
            }
        }

        List<Coord> Path = new ArrayList<Coord>();

        Path.add(new Coord(User.GoalX, User.GoalY));

        for(int i = 0;Model.Area > i;i++)
        {
            Point lowestPoint = new Point();
            int lowest = 100;

            List<Point> ValidMoves = ValidMoves(User, goalX, goalY, Movements);

            for(Point movePoint : ValidMoves)
            {
                int count = DistanceSteps[movePoint.x][movePoint.y];

                if (count < lowest)
                {
                    lowest = count;
                    lowestPoint.x = movePoint.x;
                    lowestPoint.y = movePoint.y;
                }
            }

            if (lowest != 100)
            {
                goalX = lowestPoint.x;
                goalY = lowestPoint.y;

                Path.add(new Coord(lowestPoint.x, lowestPoint.y));
            }
            else
            {
                break;
            }

            if (goalX == UserX && goalY == UserY)
            {
                break;
            }
        }
        return Path;
    }

    public void GenerateSquare(int chr, int line) // x - y
    {
        BedMatrix[chr][line] = new Coord(chr,line);
        HeightMatrix[chr][line] = 0;
        TopStackHeight[chr][line] = 0.0;

        if (Model.SqState[chr][line] == SquareState.OPEN)
        {
            Matrix[chr][line] = MatrixState.WALKABLE;
        }
        else if (chr == Model.DoorX && line == Model.DoorY)
        {
            Matrix[chr][line] = MatrixState.WALKABLE_LASTSTEP;
        }
        else
        {
            Matrix[chr][line] = MatrixState.BLOCKED;
        }

        List<RoomItem> ItemsOnSquare = GetFurniObjects(chr, line);
        for(RoomItem Item : ItemsOnSquare)
        {
            if (Item.BaseItem.Height <= 0)
            {
                continue;
            }

            if (TopStackHeight[chr][line] <= Item.Z)
            {
                TopStackHeight[chr][line] = Item.Z;

                if(Item.BaseItem.Walkable)
                {
                    Matrix[chr][line] = MatrixState.WALKABLE;
                    HeightMatrix[chr][line] = Item.BaseItem.Height;
                }
                else if(Item.BaseItem.IsSeat)
                {
                    if (Item.BaseItem.Interaction == Environment.Furniture.BED)
                    {
                        Matrix[chr][line] = MatrixState.ACOSTAR;
                        MatrixRot[chr][line] = Item.Rot;
                    }
                    else
                    {
                        Matrix[chr][line] = MatrixState.SENTAR;
                        MatrixRot[chr][line] = Item.Rot;
                    }
                }
                else
                {
                    Matrix[chr][line] = MatrixState.BLOCKED;
                }
            }


            if (Item.BaseItem.Interaction == Environment.Furniture.BED)
            {
                if (Item.Rot == 0 || Item.Rot == 4)
                {
                    BedMatrix[chr][line].y = Item.Y;
                }

                if (Item.Rot == 2 || Item.Rot == 6)
                {
                    BedMatrix[chr][line].x = Item.X;
                }
            }

        }
    }

    public void GenerateMap()
    {
        Matrix = new MatrixState[Model.MapSizeX][Model.MapSizeY];
        MatrixRot = new int[Model.MapSizeX][Model.MapSizeY];
        BB_Squares = new int[Model.MapSizeX][Model.MapSizeY];
        BedMatrix = new Coord[Model.MapSizeX][Model.MapSizeY];
        HeightMatrix = new double[Model.MapSizeX][Model.MapSizeY];
        TopStackHeight = new double[Model.MapSizeX][Model.MapSizeY];

        for (int line = 0; line < Model.MapSizeY; line++)
        {
            for (int chr = 0; chr < Model.MapSizeX; chr++)
            {
                GenerateSquare(chr, line);
            }
        }

        for (int x = 0; x < Model.MapSizeX; x++)
        {
            for (int y = 0; y < Model.MapSizeY; y++)
            {
                Squares.add(new Point(x, y));
            }
        }
    }
    
    public void SerializeFloor(RoomItem item, ServerMessage Message)
    {
        Environment.Append(item.Id, Message);
        Environment.Append(item.BaseItem.SpriteId, Message);
        Environment.Append(item.X, Message);
        Environment.Append(item.Y, Message);
        Environment.Append(item.Rot, Message);
        Environment.Append(Double.toString(item.Z).replace(",", "."), Message);
        Environment.Append(0, Message);
        Environment.Append(item.ExtraData, Message);
        Environment.Append(-1, Message);
        Environment.Append(true, Message); // knownAsUsable
    }

    public void SerializeWall(RoomItem item, ServerMessage Message)
    {
        Environment.Append(Integer.toString(item.Id), Message);
        Environment.Append(item.BaseItem.SpriteId, Message);
        Environment.Append(item.WallPos, Message);
        if(item.BaseItem.Interaction == Environment.Furniture.POSTIT)
        {
            Environment.Append(item.ExtraData.split(" ")[0], Message);
        }
        else
        {
            Environment.Append(item.ExtraData, Message);
        }
        Environment.Append(true, Message); // knownAsUsable
    }
    

    class AffectedTile
    {
        int X;
        int Y;
        int I;

        public AffectedTile(int x, int y, int i)
        {
            X = x;
            Y = y;
            I = i;
        }
    }
}
