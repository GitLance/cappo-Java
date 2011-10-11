package Requests.Marketplace;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetMarketplaceCanMakeOffer extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(611, Main.ClientMessage);
        Environment.Append(true, Main.ClientMessage);
        Environment.Append(99999, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
