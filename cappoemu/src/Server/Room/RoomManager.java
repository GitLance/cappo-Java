package Server.Room;

import Server.Connection;
import Server.DatabaseClient;
import Server.Server;
import java.sql.ResultSet;
import Server.Room.RoomModel.SquareState;
import Server.ServerMessage;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomManager
{
    private Map<Integer, Room> Rooms = new HashMap<Integer, Room>();
    private Map<String, RoomModel> Models = new HashMap<String, RoomModel>();
    private Map<String, Integer> PopularTags = new HashMap<String, Integer>();
    private Server Environment;
    private int ActiveRooms;

    public RoomManager(Server Env) throws Exception
    {
        Environment = Env;

        DatabaseClient DB = new DatabaseClient(Environment.DataBase);
        ResultSet table = DB.Query("SELECT * FROM room_models;");

        while (table.next())
        {
            RoomModel Model = new RoomModel();
            Model.Name = table.getString("id");
            Model.DoorX = table.getInt("door_x");
            Model.DoorY = table.getInt("door_y");
            Model.DoorZ = table.getInt("door_z");
            Model.DoorOrientation = table.getInt("door_dir");
            Model.Heightmap = table.getString("heightmap");
            Model.ClubOnly = (table.getInt("club_only")==1);
            Model.VipOnly = (table.getInt("vip_only")==1);

            String[] tmpHeightmap = Model.Heightmap.split(""+(char)13);
            Model.MapSizeX = tmpHeightmap[0].length();
            Model.MapSizeY = tmpHeightmap.length;
            Model.SqState = new SquareState[Model.MapSizeX][Model.MapSizeY];
            Model.SqFloorHeight = new double[Model.MapSizeX][Model.MapSizeY];
            Model.SqSeatRot = new int[Model.MapSizeX][Model.MapSizeY];

            for (int y = 0; y < Model.MapSizeY; y++)
            {
                if (y > 0)
                {
                    tmpHeightmap[y] = tmpHeightmap[y].substring(1);
                }

                for (int x = 0; x < Model.MapSizeX; x++)
                {
                    String Square = tmpHeightmap[y].substring(x, x+1).trim().toLowerCase();

                    if (Square.equals("x"))
                    {
                        Model.SqState[x][y] = SquareState.BLOCKED;
                    }
                    else
                    {
                        try
                        {
                            Model.SqFloorHeight[x][y] = Double.parseDouble(Square);
                            Model.SqState[x][y] = SquareState.OPEN;
                            Model.Area++;
                        }
                        catch(Exception ex)
                        {
                            Model.SqState[x][y] = SquareState.BLOCKED;
                        }
                    }
                }
            }

            Model.SqFloorHeight[Model.DoorX][Model.DoorY] = Model.DoorZ;
            Models.put(Model.Name, Model);
        }
        table.close();
        DB.Close();
    }


    public Room LoadRoom(int RoomId) throws Exception
    {
        DatabaseClient DB = new DatabaseClient(Environment.DataBase);
        ResultSet table2 = DB.Query("SELECT * FROM rooms WHERE id = '"+RoomId+"';");
        if(table2.next())
        {
            Room Room = new Room(Environment);
            Room.Id = table2.getInt("id");
            Room.Name = table2.getString("caption");
            Room.Description = table2.getString("description");
            Room.Owner = table2.getString("owner");
            Room.State = table2.getInt("state");
            Room.Category = table2.getInt("category");
            Room.UsersNow = 0;
            Room.UsersMax = table2.getInt("users_max");
            Room.ModelName = table2.getString("model_name");
            Room.Score = table2.getInt("score");
            String RoomTags = table2.getString("tags");
            while(RoomTags.length() > 2)
            {
                int len = Environment.b64Decode(RoomTags.substring(0,2));
                RoomTags = RoomTags.substring(2);
                AddTag(RoomTags.substring(0,len));
                RoomTags = RoomTags.substring(len);
            }
            if(table2.getInt("allow_pets")==1)
            {
                Room.SetFlag(Server.rmAllowPets,true);
            }
            if(table2.getInt("allow_pets_eat")==1)
            {
                Room.SetFlag(Server.rmAllowPetsEat,true);
            }
            if(table2.getInt("allow_walkthrough")==1)
            {
                Room.SetFlag(Server.rmAllowWalkthrough,true);
            }
            if(table2.getInt("hide_wall")==1)
            {
                Room.SetFlag(Server.rmHideWall,true);
            }
            Room.Password = table2.getString("password");
            Room.Wallpaper = table2.getString("wallpaper");
            Room.Floor = table2.getString("floor");
            Room.Landscape = table2.getString("landscape");
            Room.Model = Models.get(Room.ModelName);
            Room.UserMatrix = new boolean[Room.Model.MapSizeX][Room.Model.MapSizeY];
            Room.OwnerId = table2.getInt("owner_id");
            if(table2.getInt("SttaffPickUp")==1)
            {
                Room.SetFlag(Server.rmStaffPickUp,true);
            }
            String Icon_Items = table2.getString("icon_items");
            String[] Items = Icon_Items.split("|");
            if(Items.length == 1 && Items[0].equals(""))
            {
                Items = new String[0];
            }
            Room.Icon = new RoomIcon(table2.getInt("icon_bg"), table2.getInt("icon_fg"), Items);

            Room.BETA_ListId = new int[Room.Model.MapSizeX][Room.Model.MapSizeY];
            int index = 0;
            for(int a = 0;a<Room.Model.MapSizeX;a++)
            {
                for(int b = 0;b<Room.Model.MapSizeY;b++)
                {
                    Room.BETA_ItemsContainer.add(index, new ArrayList<RoomItem>());
                    Room.BETA_ListId[a][b] = index++;
                }
            }

            Room.LoadItems();

            Room.GenerateMap();


            Rooms.put(Room.Id, Room);

            table2.close();
            DB.Close();
            return Room;
        }
        table2.close();
        DB.Close();
        return null;
    }

    public Room CreateRoom(Connection User, String RoomName, String ModelName)
    {
        if (!Models.containsKey(ModelName))
        {
            User.SendNotif("Lo sentimos, este modelo de sala no esta disponible.");
            return null;
        }

        RoomModel Model = Models.get(ModelName);

        if (Model.ClubOnly && User.Data.Subscription.Type == 0)
        {
            User.SendNotif("Debes ser Miembro Club para poder utilizar este modelo.");
            return null;
        }

        if (Model.VipOnly && User.Data.Subscription.Type != 2)
        {
            User.SendNotif("Debes ser Miembro VIP para usar este modelo.");
            return null;
        }

        if (RoomName.length() < 3)
        {
            User.SendNotif("¡El nombre de la sala es muy corto!");
            return null;
        }


        Room Room = new Room(Environment);
        Room.SetFlag(Server.rmIsNew,true);
        Room.Id = Environment.GenerateRoomId();
        Room.Name = RoomName;
        Room.Description = "";
        Room.Owner = User.Data.UserName;
        Room.State = 0;
        Room.Category = 0;
        Room.UsersNow = 0;
        Room.UsersMax = 25;
        Room.ModelName = ModelName;
        Room.Score = 0;
        Room.SetFlag(Server.rmAllowPets,true);
        Room.SetFlag(Server.rmAllowWalkthrough,true);
        Room.Password = "";
        Room.Wallpaper = "0.0";
        Room.Floor = "0.0";
        Room.Landscape = "0.0";
        Room.Model = Models.get(Room.ModelName);
        Room.UserMatrix = new boolean[Room.Model.MapSizeX][Room.Model.MapSizeY];
        Room.OwnerId = User.Data.Id;
        Room.Icon = new RoomIcon(1, 0, new String[0]);

        Room.BETA_ListId = new int[Room.Model.MapSizeX][Room.Model.MapSizeY];
        int index = 0;
        for(int a = 0;a<Room.Model.MapSizeX;a++)
        {
            for(int b = 0;b<Room.Model.MapSizeY;b++)
            {
                Room.BETA_ItemsContainer.add(index, new ArrayList<RoomItem>());
                Room.BETA_ListId[a][b] = index++;
            }
        }

        Room.GenerateMap();

        Rooms.put(Room.Id, Room);

        User.Data.OwnRooms.add(Room.Id);

        return Room;
    }

    public int GetLoadedCount()
    {
        return Rooms.size();
    }

    public int GetActiveCount()
    {
        return ActiveRooms;
    }

    public Room GetRoom(int RoomId)
    {
        return Rooms.get(RoomId);
    }

    public void UnloadRoom(int RoomId)
    {
        Rooms.remove(RoomId);
    }

    public void SetActive(boolean active)
    {
        if(active)
        {
            ActiveRooms++;
        }
        else
        {
            ActiveRooms--;
        }
    }

    public RoomModel GetModel(String Model)
    {
        return Models.get(Model);
    }

    public void AddTag(String Tag)
    {
        int Count = 1;
        if(PopularTags.containsKey(Tag))
        {
            Count += PopularTags.get(Tag);
        }
        PopularTags.put(Tag, Count);
    }

    public void RemoveTag(String Tag)
    {
        if(PopularTags.containsKey(Tag))
        {
            int Count = PopularTags.get(Tag) - 1;
            if(Count > 0)
            {
                PopularTags.put(Tag, Count);
            }
            else
            {
                PopularTags.remove(Tag);
            }
        }
    }

    public Map<String, Integer> GetTags()
    {
        return PopularTags;
    }

    public Map<Integer, Room> GetRooms()
    {
        return Rooms;
    }

    public void SerializeRoom(ServerMessage Message, Room Room, int UsersNow, int Score)
    {
        Environment.Append(Room.Id, Message);
        if(Room.Event != null)
        {
            Environment.Append(true, Message); // is event
            Environment.Append(Room.Event.Name, Message);
        }
        else
        {
            Environment.Append(false, Message); // is event
            Environment.Append(Room.Name, Message);
        }
        Environment.Append(Room.Owner, Message);
        Environment.Append(Room.State, Message); // room state
        Environment.Append((UsersNow > 0)?UsersNow:Room.UsersNow, Message);
        Environment.Append(Room.UsersMax, Message);
        Environment.Append(Room.Description, Message);
        Environment.Append(0, Message); // ?
        if(Room.Event != null)
        {
            Environment.Append(false, Message); // can trade
            Environment.Append((Score>0)?Score:Room.Score, Message);
            Environment.Append(Room.Event.Category, Message);
            Environment.Append(Room.Event.StartTime, Message);
        }
        else
        {
            Environment.Append(true, Message); // can trade
            Environment.Append((Score>0)?Score:Room.Score, Message);
            Environment.Append(Room.Category, Message);
            Environment.Append("", Message);
        }
        Environment.Append(Room.Tags.size(), Message);
        for(String Tag : Room.Tags)
        {
            Environment.Append(Tag, Message);
        }
        Environment.Append(Room.Icon.BackgroundImage, Message);
        Environment.Append(Room.Icon.ForegroundImage, Message);
        Environment.Append(Room.Icon.Items.length, Message);
        for(int ItemId = 0;ItemId<Room.Icon.Items.length;ItemId++)
        {
            String[] values = Room.Icon.Items[ItemId].split(",");
            Environment.Append(Integer.parseInt(values[0]), Message);
            Environment.Append(Integer.parseInt(values[1]), Message);
        }
        Environment.Append((Room.Flags & Server.rmAllowPets) == Server.rmAllowPets, Message); // AllowPets
        Environment.Append(true, Message); // allow Ad
    }
}
