package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Server;
import Server.ServerMessage;

/*
 *****************
 * @author capos *
 *****************
*/

public class MottoUpdate extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        String NewMotto = Main.DecodeString();

        if(Main.Data.Motto.equals(NewMotto))
        {
            return;
        }

        Main.Data.Motto = NewMotto;

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(266, Message);
        Environment.Append(Main.Data.RoomUser.VirtualId, Message);
        Environment.Append(Main.Data.Look, Message);
        Environment.Append(Main.Data.Sex==1 ? "M" : "F", Message);
        Environment.Append(Main.Data.Motto, Message);
        Environment.Append(Main.Data.AchievementsScore, Message);
        Room.SendMessage(Message);
    }
}
