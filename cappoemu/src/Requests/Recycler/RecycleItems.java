package Requests.Recycler;

import Server.Connection;
import Server.UserItem;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class RecycleItems extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if(Environment.GetTimestamp() < Main.Data.EcotronNextTime)
        {
            return;
        }

        int count = Main.DecodeInt();

        if (count != 5)
        {
            return; // epic fail :D
        }

        for (int i = 0; i < count; i++)
        {
            UserItem Item = Main.GetItem(Main.DecodeInt());

            if (Item == null || !Item.BaseItem.AllowRecycle)
            {
                return; // epic fail :D
            }

            Main.InventoryRemoveItem(Item.Id, Item.BaseItem.Type.equals("i"));
        }

        // pending.. give item

        Environment.InitPacket(508, Main.ClientMessage);
        Environment.Append(1, Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        Main.Data.EcotronNextTime = Environment.GetTimestamp() + 300; // 5 min
    }
}
