package Requests;

import Server.Connection;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class ExtendSubscriptionPromo extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int ItemId = Main.DecodeInt();

        if (ItemId == 4896)
        {
            if(Main.Data.Credits < 10)
            {
                Environment.InitPacket(68, Main.ClientMessage);
                Environment.Append(true, Main.ClientMessage); // is credits problem
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
                return;
            }

            Environment.BuyVipOrClub(Main, 1, 1); // 1 = Club, 2 = Vip
            Main.Data.Credits -= 10;
            Environment.InitPacket(6, Main.ClientMessage);
            Environment.Append(Integer.toString(Main.Data.Credits), Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
        else if (ItemId == 4898)
        {
            if (Main.Data.Credits < 25)
            {
                Environment.InitPacket(68, Main.ClientMessage);
                Environment.Append(true, Main.ClientMessage); // is credits problem
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
            }

            Environment.BuyVipOrClub(Main, 1, 2); // 1 = Club, 2 = Vip
            Main.Data.Credits -= 25;
            Environment.InitPacket(6, Main.ClientMessage);
            Environment.Append(Integer.toString(Main.Data.Credits), Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }
}
