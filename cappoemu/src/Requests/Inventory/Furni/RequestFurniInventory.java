package Requests.Inventory.Furni;

import Server.Connection;
import Requests.Handler;
import Server.Server;
import Server.UserItem;

/*
 *****************
 * @author capos *
 *****************
*/

public class RequestFurniInventory extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(140, Main.ClientMessage);
        Environment.Append("S", Main.ClientMessage);
        Environment.Append(1, Main.ClientMessage);
        Environment.Append(1, Main.ClientMessage);
        Environment.Append(Main.Data.InventoryItems.size(), Main.ClientMessage);
        for(UserItem Item : Main.Data.InventoryItems.values())
        {
            Environment.Append(Item.Id, Main.ClientMessage);
            Environment.Append(Item.BaseItem.Type.toUpperCase(), Main.ClientMessage);
            Environment.Append(Item.Id, Main.ClientMessage);
            Environment.Append(Item.BaseItem.SpriteId, Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage); // Category
            Environment.Append(Item.ExtraData, Main.ClientMessage);
            Environment.Append(Item.BaseItem.AllowRecycle, Main.ClientMessage);
            Environment.Append(Item.BaseItem.AllowTrade, Main.ClientMessage);
            Environment.Append(Item.BaseItem.AllowInventoryStack, Main.ClientMessage);
            Environment.Append(Item.BaseItem.AllowMarketplaceSell, Main.ClientMessage);
            Environment.Append(-1, Main.ClientMessage); // infostand.rent.expiration
            Environment.Append("", Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        Environment.InitPacket(140, Main.ClientMessage);
        Environment.Append("I", Main.ClientMessage);
        Environment.Append(1, Main.ClientMessage);
        Environment.Append(1, Main.ClientMessage);
        Environment.Append(Main.Data.InventoryItemsWall.size(), Main.ClientMessage);
        for(UserItem Item : Main.Data.InventoryItemsWall.values())
        {
            int Category = 0;
            if (Item.BaseItem.ItemName.contains("wallpaper")) Category = 2; // Wallpaper
            else if (Item.BaseItem.ItemName.contains("floor_single_")) Category = 3; // Floor
            else if (Item.BaseItem.ItemName.contains("landscape")) Category = 4; // Landscape

            Environment.Append(Item.Id, Main.ClientMessage);
            Environment.Append(Item.BaseItem.Type.toUpperCase(), Main.ClientMessage);
            Environment.Append(Item.Id, Main.ClientMessage);
            Environment.Append(Item.BaseItem.SpriteId, Main.ClientMessage);
            Environment.Append(Category, Main.ClientMessage);
            Environment.Append(Item.ExtraData, Main.ClientMessage);
            Environment.Append(Item.BaseItem.AllowRecycle, Main.ClientMessage);
            Environment.Append(Item.BaseItem.AllowTrade, Main.ClientMessage);
            Environment.Append(Item.BaseItem.AllowInventoryStack, Main.ClientMessage);
            Environment.Append(Item.BaseItem.AllowMarketplaceSell, Main.ClientMessage);
            Environment.Append(-1, Main.ClientMessage); // infostand.rent.expiration
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
