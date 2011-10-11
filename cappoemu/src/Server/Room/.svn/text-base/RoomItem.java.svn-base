package Server.Room;

import Server.Furniture.Item;
import Server.ServerMessage;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomItem
{
    public int Id;
    public int RoomId;
    public int X;
    public int Y;
    public int Rot;

    public double Z;

    public int ActiveTask = 0;
    public int TaskSteps;

    public int Timer;
    public int BattleSteep;

    public String ExtraData;
    public String WallPos;

    public boolean IsFloorItem;
    public boolean IsWallItem;
    public boolean UpdateNeeded;
    public boolean DBNeedUpdate;

    public Item BaseItem;

    public Server Environment;

    public RoomItem(Server Env)
    {
        Environment = Env;
    }
    
    
    public void ProcessTask(Room Room)
    {
        if(ActiveTask == 1) // teleport close door
        {
            ExtraData = "0";
            UpdateNeeded = true;
            ActiveTask = 0;
        }
        else if(ActiveTask == 20) // timer banzai
        {
            if(--Timer>=0)
            {
                ExtraData = Integer.toString(Timer);
                UpdateNeeded = true;
                TaskSteps = 1;
            }
            else
            {
                ActiveTask = 0;
                Room.SetFlag(Server.rmBBanzai,false);
                Room.WiredManager.ParseWiredGameEnd();
                Room.ONENDBB();
            }
        }
        else if(ActiveTask == 21) // next song trax
        {
            ActiveTask = 0;
            Room.TraxPlaylist.NextSong();
            if(Room.TraxPlaylist.CurrentSong != null)
            {
                TaskSteps = (Room.TraxPlaylist.CurrentSong.Length / 1000) * 2;
                ActiveTask = 21;

                ServerMessage NotifySong = new ServerMessage();
                Environment.InitPacket(327, NotifySong);
                Environment.Append(Room.TraxPlaylist.CurrentSong.SongId, NotifySong);
                Environment.Append(Room.TraxPlaylist.SongIndex, NotifySong);
                Environment.Append(Room.TraxPlaylist.CurrentSong.SongId, NotifySong);
                Environment.Append(0, NotifySong);
                Environment.Append(0, NotifySong);
                Room.SendMessage(NotifySong);
            }

        }
    }

    public void UpdateState(Room Room)
    {
        UpdateNeeded = false;

        if (IsFloorItem)
        {
            ServerMessage Message = new ServerMessage();
            Environment.InitPacket(88, Message);
            Environment.Append(Integer.toString(Id), Message);
            Environment.Append(ExtraData, Message);
            Room.SendMessage(Message);
        }
        else if(IsWallItem)
        {
            ServerMessage Message = new ServerMessage();
            Environment.InitPacket(85, Message);
            Room.SerializeFloor(this, Message);
            Room.SendMessage(Message);
        }
    }
    
    public int[] SquareInFront()
    {
        int[] Sq = {X,Y};

        if (Rot == 0)
        {
            Sq[1]--;
        }
        else if (Rot == 2)
        {
            Sq[0]++;
        }
        else if (Rot == 4)
        {
            Sq[1]++;
        }
        else if (Rot == 6)
        {
            Sq[0]--;
        }

        return Sq;
    }
}
