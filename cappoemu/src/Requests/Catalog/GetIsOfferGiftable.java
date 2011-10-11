package Requests.Catalog;

import Server.Connection;
import Server.Player;

import Server.Furniture.Item;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetIsOfferGiftable extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Item BaseItem = Environment.Furniture.Items.get(Main.DecodeInt());

        if (BaseItem == null)
        {
            return;
        }
        
        Environment.InitPacket(622, Main.ClientMessage);
        Environment.Append(BaseItem.Id, Main.ClientMessage);
        Environment.Append(BaseItem.AllowGift, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
