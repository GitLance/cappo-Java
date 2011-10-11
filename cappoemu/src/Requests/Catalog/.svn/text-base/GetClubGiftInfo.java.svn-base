package Requests.Catalog;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetClubGiftInfo extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(623, Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage); // next club gift is available in X days
        Environment.Append(0, Main.ClientMessage); // Gifts Available
        Environment.Append(1, Main.ClientMessage); // count
        {
            Environment.Append(4850, Main.ClientMessage); // gift id
            Environment.Append("hc2_sofa", Main.ClientMessage); // item name
            Environment.Append(0, Main.ClientMessage); // Credits cost
            Environment.Append(0, Main.ClientMessage); // Pixels cost
            Environment.Append(0, Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage); // Count products
            {
                Environment.Append("s", Main.ClientMessage); // Type
                Environment.Append(3452, Main.ClientMessage); // Sprite id
                Environment.Append("", Main.ClientMessage);
                Environment.Append(2, Main.ClientMessage); // Ammount
                Environment.Append(-1, Main.ClientMessage);
            }
            Environment.Append(1, Main.ClientMessage); // Club Level
        }
        Environment.Append(1, Main.ClientMessage); // count
        {
            Environment.Append(4850, Main.ClientMessage); // gift id
            Environment.Append(false, Main.ClientMessage); // item is for vip
            Environment.Append(200, Main.ClientMessage); // days need
            Environment.Append(false, Main.ClientMessage); // Can get this
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
