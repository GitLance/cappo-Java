package Requests.Room.Engine;

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

public class UseFurniture extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        RoomItem Item = Room.GetFloorItem(Main.DecodeInt());

        if (Item == null)
        {
            return;
        }

        Item.BaseItem.Interactor.OnTrigger(Main, Item, Main.DecodeInt(), Room.CheckRights(Main.Data, false));

        Room.WiredManager.ParseWiredMutacion(Main, Item);
    }
}
