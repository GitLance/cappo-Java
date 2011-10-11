package Requests.Inventory.Furni;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Server;
import Server.ServerMessage;
import Server.UserItem;

/*
 *****************
 * @author capos *
 *****************
*/

public class RequestRoomPropertySet extends Handler
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

        UserItem Item = Main.GetWallItem(Main.DecodeInt());

        if (Item == null)
        {
            return;
        }

        String data = "";
        if(Item.BaseItem.Interaction == Environment.Furniture.WALLPAPER)
        {
            Room.Wallpaper = Item.ExtraData;
            data = "wallpaper";
        }
        else if(Item.BaseItem.Interaction == Environment.Furniture.LANDSCAPE)
        {
            Room.Landscape = Item.ExtraData;
            data = "landscape";
        }
        else if(Item.BaseItem.Interaction == Environment.Furniture.FLOOR)
        {
            Room.Floor = Item.ExtraData;
            data = "floor";
        }

        Main.InventoryRemoveItem(Item.Id, true);

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(46, Message);
        Environment.Append(data, Message);
        Environment.Append(Item.ExtraData, Message);
        Room.SendMessage(Message);
    }
}
