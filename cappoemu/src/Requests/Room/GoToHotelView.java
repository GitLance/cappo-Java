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

public class GoToHotelView extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        Main.Data.RoomUser.MoveTo(Room.Model.DoorX, Room.Model.DoorY, false);
    }
}
