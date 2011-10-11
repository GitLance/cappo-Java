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

public class UpdateNavigatorSettings extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int RoomId = Main.DecodeInt();

        Room Room = Environment.RoomManager.GetRoom(RoomId);
        if (Room == null)
        {
            return; // if is owner the room always is loaded
        }

        if(!Room.CheckRights(Main.Data, true))
        {
            return;
        }

        Main.Data.HomeRoom = RoomId;

        // Update Home Room
        Environment.InitPacket(455, Main.ClientMessage);
        Environment.Append(RoomId, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}