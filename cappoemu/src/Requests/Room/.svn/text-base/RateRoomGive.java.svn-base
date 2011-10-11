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

public class RateRoomGive extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }
        
        if (Main.Data.RatedRooms.contains(Room.Id) || Room.CheckRights(Main.Data, true))
        {
            return;
        }

        int Rating = Main.DecodeInt();

        switch (Rating)
        {
            case -1:
                Room.Score--;
                break;

            case 1:
                Room.Score++;
                break;

            default:
                return;
        }

        Main.Data.RatedRooms.add(Room.Id);

        Environment.InitPacket(345, Main.ClientMessage);
        Environment.Append(Room.Score, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
