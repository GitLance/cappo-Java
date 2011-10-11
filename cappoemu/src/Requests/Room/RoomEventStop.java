package Requests.Room;

import Server.Connection;
import Server.Room.Room;
import Server.ServerMessage;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomEventStop extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null || !Room.CheckRights(Main.Data, true) || Room.Event == null)
        {
            return;
        }

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(370, Message);
        Environment.Append("-1", Message);
        Room.SendMessage(Message);

        Room.Event = null;
    }
}
