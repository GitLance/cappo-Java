package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Room.RoomItem;
import Server.Server;
import Server.UserItem;

/*
 *****************
 * @author capos *
 *****************
*/

public class ItemPlace extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null || !Room.CheckRights(Main.Data, false))
        {
            return;
        }

        String PlacementData = Main.DecodeString();
        String[] DataBits = PlacementData.split(" ");
        int ItemId = Integer.parseInt(DataBits[0]);

        if (DataBits[1].startsWith(":")) // Wall Item
        {
            UserItem Item = Main.GetWallItem(ItemId);

            if (Item == null)
            {
                return;
            }

            if(Item.BaseItem.Interaction == Environment.Furniture.DIMMER)
            {
                if(Room.MoodlightData != null) // already have dimmer !
                {
                    return;
                }
            }

            if (!DataBits[3].equals("l") && !DataBits[3].equals("r"))
            {
                Environment.InitPacket(516, Main.ClientMessage);
                Environment.Append(11, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
                return;
            }

            String[] widD = DataBits[1].substring(3).split(",");
            int widthX = Integer.parseInt(widD[0]);
            int widthY = Integer.parseInt(widD[1]);
            if (widthX < 0 || widthY < 0 || widthX > 200 || widthY > 200)
            {
                Environment.InitPacket(516, Main.ClientMessage);
                Environment.Append(11, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
                return;
            }

            String[] lenD = DataBits[2].substring(2).split(",");
            int lengthX = Integer.parseInt(lenD[0]);
            int lengthY = Integer.parseInt(lenD[1]);
            if (lengthX < 0 || lengthY < 0 || lengthX > 200 || lengthY > 200)
            {
                Environment.InitPacket(516, Main.ClientMessage);
                Environment.Append(11, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
                return;
            }

            RoomItem RoomItem = new RoomItem(Environment);
            RoomItem.Id = Item.Id;
            RoomItem.RoomId = Main.Data.CurrentRoom;
            RoomItem.BaseItem = Item.BaseItem;
            RoomItem.ExtraData = Item.ExtraData;
            RoomItem.X = 0;
            RoomItem.Y = 0;
            RoomItem.Z = 0.0;
            RoomItem.Rot = 0;
            RoomItem.WallPos = ":w=" + widthX + "," + widthY + " " + "l=" + lengthX + "," + lengthY + " " + DataBits[3];

            if (Room.SetWallItem(Main, RoomItem, true))
            {
                Main.InventoryRemoveItem(ItemId, true);
            }
        }
        else // Floor Item
        {
            UserItem Item = Main.GetFloorItem(ItemId);

            if (Item == null)
            {
                return;
            }

            int X = Integer.parseInt(DataBits[1]);
            int Y = Integer.parseInt(DataBits[2]);
            int Rot = Integer.parseInt(DataBits[3]);

            RoomItem RoomItem = new RoomItem(Environment);
            RoomItem.Id = Item.Id;
            RoomItem.RoomId = Main.Data.CurrentRoom;
            RoomItem.BaseItem = Item.BaseItem;
            RoomItem.ExtraData = Item.ExtraData;
            RoomItem.X = 0;
            RoomItem.Y = 0;
            RoomItem.Z = 0.0;
            RoomItem.Rot = 0;
            RoomItem.WallPos = "";

            if (Room.SetFloorItem(Main, RoomItem, X, Y, Rot, true))
            {
                if(Item.BaseItem.Interaction == Environment.Furniture.TELEPORT)
                {
                    Environment.Teleports.SetRoom(Item.Id, Room.Id);
                }

                Main.InventoryRemoveItem(ItemId, false);
            }
        }
    }
}
