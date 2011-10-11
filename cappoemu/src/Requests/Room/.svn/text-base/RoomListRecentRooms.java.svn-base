package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomListRecentRooms extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        // pending..
        Environment.InitPacket(451, Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage);
        Environment.Append(Integer.toString(7), Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage);
        Environment.Append(false, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}