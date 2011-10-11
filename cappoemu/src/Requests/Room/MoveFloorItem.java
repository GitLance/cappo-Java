package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Room.RoomItem;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
 */
public class MoveFloorItem extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        if (!Room.CheckRights(Main.Data, false))
        {
            return;
        }

        RoomItem Item = Room.GetFloorItem(Main.DecodeInt());

        if (Item == null)
        {
            return;
        }

        int x = Main.DecodeInt();
        int y = Main.DecodeInt();
        int Rotation = Main.DecodeInt();

        Room.SetFloorItem(Main, Item, x, y, Rotation, false);
    }
}
