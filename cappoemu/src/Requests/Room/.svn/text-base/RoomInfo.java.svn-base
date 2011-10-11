package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomInfo extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int RoomId = Main.DecodeInt();

        Room Room = Environment.RoomManager.GetRoom(RoomId);

        if (Room == null)
        {
            return;
        }

        Environment.InitPacket(454, Main.ClientMessage);
        Environment.Append(true, Main.ClientMessage);
        Room.Serialize(Main.ClientMessage);
        Environment.Append(false, Main.ClientMessage); // roomForward
        Environment.Append((Room.Flags & Server.rmStaffPickUp) == Server.rmStaffPickUp, Main.ClientMessage); // pickup staff
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
