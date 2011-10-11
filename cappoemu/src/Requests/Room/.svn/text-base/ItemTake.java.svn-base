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

public class ItemTake extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }
        
        if (!Room.CheckRights(Main.Data, true))
        {
            return;
        }

        int junk = Main.DecodeInt();

        RoomItem Item = Room.GetItem(Main.DecodeInt());

        if (Item == null)
        {
            return;
        }

        if(Item.BaseItem.Interaction == Environment.Furniture.POSTIT)
        {
            return; // not allowed to pick up post.its
        }

        Room.RemoveFurniture(Main, Item);
        Main.AddItem(Item.Id, Item.ExtraData, Item.BaseItem, true);

        if(Item.BaseItem.Interaction == Environment.Furniture.TELEPORT)
        {
            Environment.Teleports.DelRoom(Item.Id);
        }

        // Update Inventory
        Environment.InitPacket(101, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
