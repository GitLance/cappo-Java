package Requests.Navigator;

import Server.Connection;
import Server.Room.Room;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class CanCreateEvent extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        if (!Room.CheckRights(Main.Data, true))
        {
            return;
        }

        if (Room.State != 0)
        {
            Environment.InitPacket(367, Main.ClientMessage);
            Environment.Append(false, Main.ClientMessage);
            Environment.Append(3, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        Environment.InitPacket(367, Main.ClientMessage);
        Environment.Append(true, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
