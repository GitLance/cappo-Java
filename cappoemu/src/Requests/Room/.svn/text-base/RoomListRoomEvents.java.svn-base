package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomListRoomEvents extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        // pending..
        int Category = Integer.parseInt(Main.DecodeString());
        Environment.InitPacket(451, Main.ClientMessage);
        Environment.Append(Category, Main.ClientMessage);
        Environment.Append(Integer.toString(12), Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage);
        Environment.Append(false, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
