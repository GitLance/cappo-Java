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

public class Wave extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        Main.Data.RoomUser.IdleTime = 0;

        Main.Data.RoomUser.DanceId = 0;

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(481, Message);
        Environment.Append(Main.Data.RoomUser.VirtualId, Message);
        Room.SendMessage(Message);
    }
}
