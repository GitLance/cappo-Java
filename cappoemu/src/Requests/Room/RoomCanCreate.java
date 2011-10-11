package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomCanCreate extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if(Main.Data.OwnRooms.size() < Main.Data.MaxRooms)
        {
            Environment.InitPacket(512, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
        else
        {
            Environment.InitPacket(512, Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage);
            Environment.Append(Main.Data.MaxRooms, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }
}
