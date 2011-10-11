package Server;

import Server.Room.RoomItem;
import Server.Room.RoomUser;
import Server.Room.Room;

/*
 *****************
 * @author capos *
 *****************
*/

public class WiredManager
{
    private Server Environment;
    private Room Room;

    public int MAX_CHILDS_PER_WIRED = 5;
    private int MAX_WIREDS_PER_ROOM = 18;

    public String[] WiredData;
    public int[][] WiredOptions;
    public int[] EffectDelay;

    private int[] WiredType;
    private int[][] WiredPos;
    private RoomItem[] WiredItem;
    private RoomItem[][] Items;
    private int count = 0;

    private String[][] ItemOriginal_ExtraData;
    private int[][] ItemOriginal_X;
    private int[][] ItemOriginal_Y;
    private double[][] ItemOriginal_Z;
    private int[][] ItemOriginal_Rot;
    private String[][] ItemOriginal_WallPos;

    public WiredManager(Room Room, Server Environment)
    {
        this.Environment = Environment;
        this.Room = Room;

        EffectDelay = new int[MAX_WIREDS_PER_ROOM];
        WiredItem = new RoomItem[MAX_WIREDS_PER_ROOM];
        WiredData = new String[MAX_WIREDS_PER_ROOM];
        WiredOptions = new int[MAX_WIREDS_PER_ROOM][];
        WiredType = new int[MAX_WIREDS_PER_ROOM];
        WiredPos = new int[MAX_WIREDS_PER_ROOM][2];
        Items = new RoomItem[MAX_WIREDS_PER_ROOM][];
        ItemOriginal_ExtraData = new String[MAX_WIREDS_PER_ROOM][];
        ItemOriginal_X = new int[MAX_WIREDS_PER_ROOM][];
        ItemOriginal_Y = new int[MAX_WIREDS_PER_ROOM][];
        ItemOriginal_Z = new double[MAX_WIREDS_PER_ROOM][];
        ItemOriginal_Rot = new int[MAX_WIREDS_PER_ROOM][];
        ItemOriginal_WallPos = new String[MAX_WIREDS_PER_ROOM][];
    }


    public boolean RegisterWired(RoomItem Item, int type)
    {
        if (count >= MAX_WIREDS_PER_ROOM)
        {
            return false;
        }

        Items[count] = new RoomItem[MAX_CHILDS_PER_WIRED];
        ItemOriginal_ExtraData[count] = new String[MAX_CHILDS_PER_WIRED];
        ItemOriginal_X[count] = new int[MAX_CHILDS_PER_WIRED];
        ItemOriginal_Y[count] = new int[MAX_CHILDS_PER_WIRED];
        ItemOriginal_Z[count] = new double[MAX_CHILDS_PER_WIRED];
        ItemOriginal_Rot[count] = new int[MAX_CHILDS_PER_WIRED];
        ItemOriginal_WallPos[count] = new String[MAX_CHILDS_PER_WIRED];
        WiredItem[count] = Item;
        WiredType[count] = type;
        WiredData[count] =  "";

        UpdatedPos(count++, Item.X, Item.Y);
        return true;
    }

    public int GetIdWired(RoomItem Item)
    {
        int p_count = count;
        for (int i = 0; i < p_count; i++)
        {
            if (WiredItem[i] == Item)
            {
                return i;
            }
        }
        return 9999;
    }

    private int[] GetWiredsInSquare(int[] Pos)
    {
        int p_count = count;
        int[] Wireds = new int[MAX_WIREDS_PER_ROOM];
        int cur = 0;
        for (int i = 0; i < p_count; i++)
        {
            if (WiredPos[i][0] == Pos[0])
            {
                if (WiredPos[i][1] == Pos[1])
                {
                    Wireds[cur++] = i;
                }
            }
        }
        Wireds[cur] = -1; // end..

        return Wireds;
    }

    private void UpdatedPos(int id, int X, int Y)
    {
        WiredPos[id][0] = X;
        WiredPos[id][1] = Y;
    }

    public boolean SetWiredPos(RoomItem ItemId)
    {
        int id = GetIdWired(ItemId);
        if (id == 9999)
        {
            return false;
        }

        UpdatedPos(id, ItemId.X, ItemId.Y);

        return true;
    }

    public void AddChild(int id, int i, RoomItem Child)
    {
        if (i < 1)
        {
            for (int i_ = 0; i_ < MAX_CHILDS_PER_WIRED; i_++)
            {
                Items[id][i_] = null;
            }
        }
        if (Child == null) return;

        Items[id][i] = Child;
        ItemOriginal_ExtraData[id][i] = Child.ExtraData;
        ItemOriginal_X[id][i] = Child.X;
        ItemOriginal_Y[id][i] = Child.Y;
        ItemOriginal_Z[id][i] = Child.Z;
        ItemOriginal_Rot[id][i] = Child.Rot;
        ItemOriginal_WallPos[id][i] = Child.WallPos;
    }

    public void SetDelay(int id, int Delay)
    {
        EffectDelay[id] = Delay;
    }

    public RoomItem[] GetWiredChilds(int id)
    {
        return Items[id];
    }


    public boolean ProcesarEffect(Connection User, int[] WiredsHere, boolean detoned)
    {
        for (int i_ = 0; i_ < WiredsHere.length; i_++)
        {
            if (WiredsHere[i_] == -1) break;
            if (WiredType[WiredsHere[i_]] != 3) continue; // Effect

            detoned = true;

            ServerMessage Message = new ServerMessage();

            switch (WiredItem[WiredsHere[i_]].BaseItem.WiredType)
            {
                case 7: // Show Message
                    Environment.InitPacket(25, Message);
                    Environment.Append(User.Data.RoomUser.VirtualId, Message);
                    Environment.Append(WiredData[WiredsHere[i_]], Message);
                    Environment.Append(0, Message);
                    Environment.EndPacket(User.Socket, Message);
                    break;
                case 13: // Move And Rotate Furni
                    if(WiredOptions[WiredsHere[i_]].length == 2) // prevent hack..
                    {
                        int x = 0;
                        int y = 0;
                        if(WiredOptions[WiredsHere[i_]][0] == 0)
                        {
                            y--;
                        }
                        else if(WiredOptions[WiredsHere[i_]][0] == 1)
                        {
                            x++;
                        }
                        else if(WiredOptions[WiredsHere[i_]][0] == 2)
                        {
                            y++;
                        }
                        else if(WiredOptions[WiredsHere[i_]][0] == 3)
                        {
                            x--;
                        }

                        for (int i__ = 0; i__ < MAX_CHILDS_PER_WIRED; i__++)
                        {
                            if (Items[WiredsHere[i_]][i__] == null) continue;

                            // pending.. hacerlo con efecto de moverse

                            x += Items[WiredsHere[i_]][i__].X;
                            y += Items[WiredsHere[i_]][i__].Y;

                            int rot = Items[WiredsHere[i_]][i__].Rot;

                            Room.SetFloorItem(null, Items[WiredsHere[i_]][i__], x, y, rot, false);
                        }
                    }
                    break;
                case 3408:
                    for (int i__ = 0; i__ < MAX_CHILDS_PER_WIRED; i__++)
                    {
                        if (Items[WiredsHere[i_]][i__] == null) continue;

                        Items[WiredsHere[i_]][i__].X = ItemOriginal_X[WiredsHere[i_]][i__];
                        Items[WiredsHere[i_]][i__].Y = ItemOriginal_Y[WiredsHere[i_]][i__];
                        Items[WiredsHere[i_]][i__].Z = ItemOriginal_Z[WiredsHere[i_]][i__];
                        Items[WiredsHere[i_]][i__].Rot = ItemOriginal_Rot[WiredsHere[i_]][i__];
                        Items[WiredsHere[i_]][i__].WallPos = ItemOriginal_WallPos[WiredsHere[i_]][i__];
                        Items[WiredsHere[i_]][i__].ExtraData = ItemOriginal_ExtraData[WiredsHere[i_]][i__];
                        Items[WiredsHere[i_]][i__].UpdateNeeded = true;
                    }
                    break;

                case 3382:
                    int r = 0;
                    do
                    {
                        r = Environment.GetRandomNumber(0, MAX_CHILDS_PER_WIRED - 1);
                    }
                    while (Items[WiredsHere[i_]][r] == null);

                    User.Data.RoomUser.ClearMovement(false);
                    User.Data.RoomUser.X = Items[WiredsHere[i_]][r].X;
                    User.Data.RoomUser.Y = Items[WiredsHere[i_]][r].Y;
                    User.Data.RoomUser.Z = Items[WiredsHere[i_]][r].Z;
                    Room.UpdateUserStatus(User.Data.RoomUser, true, null, true);
                    User.Data.RoomUser.UpdateNeeded = true;

                    break;

                case 3393:
                    for (int i__ = 0; i__ < MAX_CHILDS_PER_WIRED; i__++)
                    {
                        if (Items[WiredsHere[i_]][i__] == null) continue;

                        Items[WiredsHere[i_]][i__].BaseItem.Interactor.OnTrigger(User, Items[WiredsHere[i_]][i__], -1, true);
                    }
                    break;
            }
        }
        return detoned;
    }

    public boolean ParseWiredSay(Connection User, String Message)
    {
        int p_count = count;
        boolean detoned = false;
        for (int i = 0; i < p_count; i++)
        {
            if (WiredType[i] != 2) continue; // triggers

            if (WiredItem[i].BaseItem.WiredType != 0) continue; // User Says Keyword

            if (!WiredData[i].isEmpty() && Message.toLowerCase().contains(WiredData[i].toLowerCase()))
            {
                int[] WiredsHere = GetWiredsInSquare(WiredPos[i]);

                for (int i_ = 0; i_ < WiredsHere.length; i_++)
                {
                    if (WiredsHere[i_] == -1) break;
                    if (WiredType[WiredsHere[i_]] != 1) continue; // condiciones

                    if (ProcessIf(WiredsHere[i_]))
                    {
                        i += 999;
                        break;
                    }
                }

                if (i >= 999)
                {
                    i -= 999;
                    continue;
                }

                detoned = ProcesarEffect(User, WiredsHere, detoned);
            }
        }

        return detoned;
    }


    public void ParseWiredGameStart()
    {
        int p_count = count;
        for (int i = 0; i < p_count; i++)
        {
            if (WiredType[i] != 2) continue; // triggers

            if (WiredItem[i].BaseItem.WiredType != 8) continue; // Game Starts

            int[] WiredsHere = GetWiredsInSquare(WiredPos[i]);

            for (int i_ = 0; i_ < WiredsHere.length; i_++)
            {
                if (WiredsHere[i_] == -1) break;
                if (WiredType[WiredsHere[i_]] != 1) continue; // condiciones

                if (ProcessIf(WiredsHere[i_]))
                {
                    i += 999;
                    break;
                }
            }

            if (i >= 999)
            {
                i -= 999;
                continue;
            }

            RoomUser[] List = Room.UserList.clone();

            for (int i_ = 0; i_ < count; i_++)
            {
                if (List[i_] != null)
                {
                    ProcesarEffect(List[i_].Client, WiredsHere, true);
                }
            }
        }
    }

    public void ParseWiredWalksOnFurni(Connection User, RoomItem Item)
    {
        int p_count = count;
        for (int i = 0; i < p_count; i++)
        {
            if (WiredType[i] != 2) continue; // triggers

            if (WiredItem[i].BaseItem.WiredType != 1) continue; // User Walks On Furni

            for (int i__ = 0; i__ < MAX_CHILDS_PER_WIRED; i__++)
            {
                if (Items[i][i__] != Item) continue;

                i += 888;
                break;
            }

            if (i < 888)
            {
                continue;
            }
            i -= 888;

            int[] WiredsHere = GetWiredsInSquare(WiredPos[i]);

            for (int i_ = 0; i_ < WiredsHere.length; i_++)
            {
                if (WiredsHere[i_] == -1) break;
                if (WiredType[WiredsHere[i_]] != 1) continue; // condiciones

                if (ProcessIf(WiredsHere[i_]))
                {
                    i += 999;
                    break;
                }
            }

            if (i >= 999)
            {
                i -= 999;
                continue;
            }

            ProcesarEffect(User, WiredsHere, true);
        }
    }

    public void ParseWiredEntersRoom(Connection User)
    {
        int p_count = count;
        for (int i = 0; i < p_count; i++)
        {
            if (WiredType[i] != 2) continue; // triggers

            if (WiredItem[i].BaseItem.WiredType != 7) continue; // User Enters Room

            if (!WiredData[i].isEmpty() && !User.Data.UserName.toLowerCase().equals(WiredData[i].toLowerCase()))
            {
                continue; // filter by name..
            }

            int[] WiredsHere = GetWiredsInSquare(WiredPos[i]);

            for (int i_ = 0; i_ < WiredsHere.length; i_++)
            {
                if (WiredsHere[i_] == -1) break;
                if (WiredType[WiredsHere[i_]] != 1) continue; // condiciones

                if (ProcessIf(WiredsHere[i_]))
                {
                    i += 999;
                    break;
                }
            }

            if (i >= 999)
            {
                i -= 999;
                continue;
            }

            ProcesarEffect(User, WiredsHere, true);
        }
    }











    
    public void ParseWiredMutacion(Connection User, RoomItem Item)
    {
        int p_count = count;
        for (int i = 0; i < p_count; i++)
        {
            if (WiredType[i] != 2) continue; // triggers

            if (WiredItem[i].BaseItem.SpriteId != 3376) continue; // Causante mutacion

            for (int i__ = 0; i__ < MAX_CHILDS_PER_WIRED; i__++)
            {
                if (Items[i][i__] != Item) continue;

                i += 888;
                break;
            }
            if (i < 888)
            {
                continue;
            }
            i -= 888;

            int[] WiredsHere = GetWiredsInSquare(WiredPos[i]);

            for (int i_ = 0; i_ < WiredsHere.length; i_++)
            {
                if (WiredsHere[i_] == -1) break;
                if (WiredType[WiredsHere[i_]] != 1) continue; // condiciones

                if (ProcessIf(WiredsHere[i_]))
                {
                    i += 999;
                    break;
                }
            }

            if (i >= 999)
            {
                i -= 999;
                continue;
            }

            ProcesarEffect(User, WiredsHere, true);
        }
    }

    public void ParseWiredVete(Connection User, RoomUser RoomUser, RoomItem Item)
    {
        int p_count = count;
        for (int i = 0; i < p_count; i++)
        {
            if (WiredType[i] != 2) continue; // triggers

            if (WiredItem[i].BaseItem.SpriteId != 3386) continue; // Causante Vete

            for (int i__ = 0; i__ < MAX_CHILDS_PER_WIRED; i__++)
            {
                if (Items[i][i__] != Item) continue;

                i += 888;
                break;
            }

            if (i < 888)
            {
                continue;
            }
            i -= 888;

            int[] WiredsHere = GetWiredsInSquare(WiredPos[i]);

            for (int i_ = 0; i_ < WiredsHere.length; i_++)
            {
                if (WiredsHere[i_] == -1) break;
                if (WiredType[WiredsHere[i_]] != 1) continue; // condiciones

                if (ProcessIf(WiredsHere[i_]))
                {
                    i += 999;
                    break;
                }
            }

            if (i >= 999)
            {
                i -= 999;
                continue;
            }

            ProcesarEffect(User, WiredsHere, true);
        }
    }

    public void ParseWiredGameEnd()
    {
        int p_count = count;
        for (int i = 0; i < p_count; i++)
        {
            if (WiredType[i] != 2) continue; // triggers

            if (WiredItem[i].BaseItem.SpriteId != 3388) continue; // Game Ends

            int[] WiredsHere = GetWiredsInSquare(WiredPos[i]);

            for (int i_ = 0; i_ < WiredsHere.length; i_++)
            {
                if (WiredsHere[i_] == -1) break;
                if (WiredType[WiredsHere[i_]] != 1) continue; // condiciones

                if (ProcessIf(WiredsHere[i_]))
                {
                    i += 999;
                    break;
                }
            }

            if (i >= 999)
            {
                i -= 999;
                continue;
            }

            RoomUser[] List = Room.UserList.clone();

            for (int i_ = 0; i_ < count; i_++)
            {
                if (List[i_] != null)
                {
                    ProcesarEffect(List[i_].Client, WiredsHere, true);
                }
            }
        }
    }

    private boolean ProcessIf(int id)
    {
        boolean block = false;

        /*
         * pending...
         *
        */

        return block;
    }

}
