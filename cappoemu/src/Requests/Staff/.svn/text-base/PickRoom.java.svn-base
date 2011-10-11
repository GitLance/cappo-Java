package Requests.Staff;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class PickRoom extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.DecodeInt());
        if (Room == null) return;

        if((Room.Flags & Server.rmStaffPickUp) == Server.rmStaffPickUp)
        {
            Room.SetFlag(Server.rmStaffPickUp,false);
        }
        else
        {
            Room.SetFlag(Server.rmStaffPickUp,true);
        }

        
        // pending..

    }
}
