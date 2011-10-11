package Server.Room;

import Server.BotAI;
import Server.Connection;
import Server.Pet;
import Server.ServerMessage;
import Server.Server;
import java.util.ArrayList;
import java.util.List;
/*
 *****************
 * @author capos *
 *****************
*/

public class RoomUser
{
    public Connection Client;

    public boolean IsAsleep;
    public boolean IsWalking;
    public boolean SetStep;
    public boolean AllowOverride;
    public boolean UpdateNeeded;
    public boolean IsDancing;
    public boolean CheckSquare;
    public boolean PathRecalcNeeded;

    public int Id;
    public int VirtualId;
    public int RoomId;
    public int X;
    public int Y;
    public int RotHead;
    public int RotBody;
    public int DanceId;
    public int BattleBallType;
    public int IdleTime;
    public int CarryItemID;
    public int CarryTimer;
    public int SetX;
    public int SetY;
    public int PathStep;
    public int PathRecalcX;
    public int PathRecalcY;
    public int GoalX;
    public int GoalY;
    public int FloodCount = 0;
    public int Type = 1; // 1 - user , 2 - pet , 3 bot
    public int Ping_Steep = 0;

    public long Last_Say = 0;

    public double Z;
    public double SetZ;

    public RoomBot BotData;
    public BotAI BotAI;
    public Pet PetData;

    public List<Coord> Path;
    public String Status = "";
    public boolean isOwnerAdmin;
    public boolean HavePowers;

    public int ActiveTask;
    public int TaskSteps;
    public int UserIndex;
    public int CurrentEffect;
    public int EffectStatus;
    public boolean IsBuyEffect;

    public RoomUser(Connection User, int RoomId, int VirtualId, int UserIndex)
    {
        if (User != null) // User (No bots,pets)
        {
            this.Id = User.Data.Id;
        }

        this.UserIndex = UserIndex;
        this.Client = User;
        this.RoomId = RoomId;
        this.VirtualId = VirtualId;
        this.IdleTime = 0;
        this.X = 0;
        this.Y = 0;
        this.Z = 0;
        this.RotHead = 0;
        this.RotBody = 0;
        this.UpdateNeeded = true;
        this.Path = new ArrayList<Coord>();
        this.PathStep = 0;
        this.BattleBallType = 0;

        this.AllowOverride = false;
    }

    public void SetPos(int X, int Y, double Z)
    {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public void SetRot(int Rotation)
    {
        SetRot(Rotation, false);
    }
    
    public void SetRot(int Rotation, boolean HeadOnly)
    {
        if (Status.contains("lay") || IsWalking)
        {
            return;
        }

        int diff = this.RotBody - Rotation;

        this.RotHead = this.RotBody;

        if (Status.contains("sit") || HeadOnly)
        {
            if (RotBody == 2 || RotBody == 4)
            {
                if (diff > 0)
                {
                    RotHead = RotBody - 1;
                }
                else if (diff < 0)
                {
                    RotHead = RotBody + 1;
                }
            }
            else if (RotBody == 0 || RotBody == 6)
            {
                if (diff > 0)
                {
                    RotHead = RotBody - 1;
                }
                else if (diff < 0)
                {
                    RotHead = RotBody + 1;
                }
            }
        }
        else if (diff <= -2 || diff >= 2)
        {
            this.RotHead = Rotation;
            this.RotBody = Rotation;
        }
        else
        {
            this.RotHead = Rotation;
        }

        this.UpdateNeeded = true;
    }

    public void Chat(Connection User, Room Room, String Text, boolean Shout)
    {
        ServerMessage Message = new ServerMessage();
        if (Type == 1)
        {
            if ((User.Data.Flags & Server.plrMuted) == Server.plrMuted)
            {
                User.SendNotif("You are muted.");
                return;
            }

            Text = Text.trim();

            if(Text.length() > 100)
            {
                return;
            }
            
            long ticks = System.currentTimeMillis();
            if(FloodCount == -1)
            {
                if (Last_Say > ticks)
                {
                    return;
                }
                FloodCount = 0;
            }
            else
            {
                Last_Say += 2000;
                if (Last_Say > ticks)
                {
                    if (++FloodCount > 4)
                    {
                        int Time = 20;
                        
                        User.Environment.InitPacket(27, Message);
                        User.Environment.Append(Time, Message);
                        User.Environment.EndPacket(User.Socket, Message);
                        
                        FloodCount = -1;
                        Last_Say = ticks + (Time * 1000);
                        return;
                    }
                }
                else
                {
                    FloodCount = 0;
                }
            }
            Last_Say = ticks;

            if (Room.WiredManager.ParseWiredSay(User, Text))
            {
                return;
            }

            if (Text.charAt(0) == ':' && ParseCmd(User, Text.substring(1)))
            {
                return;
            }

            /*ThisRoom.TurnHeads(X, Y, HabboId);*/

            Text = ParseMessage(Text);
            
            User.Environment.InitPacket(Shout ? 26 : 24, Message);
            User.Environment.Append(VirtualId, Message);
            User.Environment.Append(Text, Message);
            User.Environment.Append(GetSpeechEmotion(Text), Message);
            User.Environment.Append(Urls.toArray().length, Message);
            for(String Link : Urls)
            {
                if(Link == null) break;
                User.Environment.Append("/link_to?url="+Link, Message);
                User.Environment.Append(Link, Message);
                User.Environment.Append(1, Message);
            }
            Room.SendMessage(Message);

            Room.OnUserSay(this, Text);
        }
        else
        {
            User.Environment.InitPacket(24, Message);
            User.Environment.Append(VirtualId, Message);
            User.Environment.Append(Text, Message);
            User.Environment.Append(GetSpeechEmotion(Text), Message);
            Room.SendMessage(Message);
        }

    }

    private List<String> Urls;
    private char[] HttP = {'h','t','t','p'};
    private char[] Separator = {':','/','/'};

    private String ParseMessage(String Message)
    {
        int len = Message.length();

        int a = 0;
        int UrlCount = 0;

        Urls = new ArrayList<String>();

        String NewMsg = "";

        int pos = 0;
        for(;pos<len;pos++)
        {
            if (HttP[a] == Message.charAt(pos))
            {
                if(++a == 4) // http len
                {
                    a = 0;

                    if(len > pos + 4)
                    {
                        if (Separator[a] == Message.charAt(++pos)) // http
                        {
                            if (Separator[++a] == Message.charAt(++pos))
                            {
                                if (Separator[++a] == Message.charAt(++pos))
                                {
                                    int init = pos - 6;
                                    do
                                    {
                                        if(++pos<len)
                                        {
                                            if (' ' == Message.charAt(pos))
                                            {
                                                break;
                                            }
                                        }
                                        else break;
                                    }
                                    while(true);

                                    NewMsg += Message.substring(0,init) + "{" + (UrlCount++) + "}";
                                    Urls.add(Message.substring(init, pos));
                                    Message = Message.substring(pos);
                                    len -= pos;
                                    pos = 0;
                                }
                            }
                        }
                        else if (Separator[a] == Message.charAt(++pos)) // https
                        {
                            if (Separator[++a] == Message.charAt(++pos))
                            {
                                if (Separator[++a] == Message.charAt(++pos))
                                {
                                    int init = pos - 7;
                                    do
                                    {
                                        if(++pos<len)
                                        {
                                            if (' ' == Message.charAt(pos))
                                            {
                                                break;
                                            }
                                        }
                                        else break;
                                    }
                                    while(true);

                                    NewMsg += Message.substring(0,init) + "{" + (UrlCount++) + "}";
                                    Urls.add(Message.substring(init, pos));
                                    Message = Message.substring(pos);
                                    len -= pos;
                                    pos = 0;
                                }
                            }
                        }
                    }

                    a = 0;
                }
            }
            else
            {
                a = 0;
            }
        }

        if(len > 0)
        {
            NewMsg += Message.substring(0,pos);
        }
        
        return NewMsg;
    }

    private int GetSpeechEmotion(String Message)
    {
        Message = Message.toLowerCase();

        if (Message.contains(":)") || Message.contains(":d") || Message.contains("=]") ||   Message.contains("=d") || Message.contains(":>"))
        {
            return 1;
        }
        if (Message.contains(">:(") || Message.contains(":@"))
        {
            return 2;
        }
        if (Message.contains(":o"))
        {
            return 3;
        }
        if (Message.contains(":(") || Message.contains("=[") || Message.contains(":'(") || Message.contains("='["))
        {
            return 4;
        }
        return 0;
    }
    
    public void CarryItem(Room Room, int Item)
    {
        this.CarryItemID = Item;
        this.CarryTimer = (Item > 0) ? 240 : 0;

        ServerMessage Message = new ServerMessage();
        Room.Environment.InitPacket(482, Message);
        Room.Environment.Append(VirtualId, Message);
        Room.Environment.Append(Item, Message);
        Room.SendMessage(Message);
    }

    public void ClearMovement(boolean Update)
    {
        IsWalking = false;
        PathRecalcNeeded = false;
        CheckSquare = false;
        Path = new ArrayList<Coord>();
        if(Status.contains("mv"))
        {
            Status = "";
        }
        GoalX = 0;
        GoalY = 0;
        SetStep = false;
        SetX = 0;
        SetY = 0;
        SetZ = 0;

        if (Update)
        {
            UpdateNeeded = true;
        }
    }

    public void SetStatus(String Key, String Value)
    {
        Status = Key+" "+Value+"/";
        UpdateNeeded = true;
    }

    public void RemoveStatus()
    {
        Status = "";
        UpdateNeeded = true;
    }

    public boolean HaveStatus(String Key)
    {
        return Status.contains(Key);
    }

    public void MoveTo(int X, int Y, boolean check)
    {
        CheckSquare = check;
        PathRecalcNeeded = true;
        PathRecalcX = X;
        PathRecalcY = Y;
    }

    public void ProcessTask(Room Room)
    {
        if(ActiveTask == 1)
        {
            ActiveTask = 0;
            
            AllowOverride = false;

            int TeleRoomId = Room.Environment.Teleports.GetRoom(Client.Data.TeleporterId);
            if (TeleRoomId != -1)
            {
                if(RoomId == TeleRoomId)
                {
                    RoomItem Item = Room.GetFloorItem(Client.Data.TeleporterId);

                    if (Item != null)
                    {
                        SetPos(Item.X, Item.Y, Item.Z);
                        SetRot(Item.Rot);

                        Item.ExtraData = "2";
                        Item.UpdateNeeded = true;
                        
                        Item.TaskSteps = 2;
                        Item.ActiveTask = 1;

                        TaskSteps = 3;
                        ActiveTask = 2;
                    }
                }
                else
                {
                    Client.Data.SetFlag(Server.plrTeleporting,true);
                    Client.LoadRoom(TeleRoomId, "");
                }
            }
        }
        else if(ActiveTask == 2)
        {
            ActiveTask = 0;

            RoomItem Item = Room.GetFloorItem(Client.Data.TeleporterId);
            Client.Data.TeleporterId = 0;
            if (Item != null)
            {
                int[] xy = Item.SquareInFront();
                MoveTo(xy[0], xy[1], true);
            }
        }
    }

    private boolean ParseCmd(Connection User, String cmd)
    {
        String[] parsed = cmd.split(" ");

        if(parsed[0].equals("test1"))
        {
            
        }

        return false;
    }
}
