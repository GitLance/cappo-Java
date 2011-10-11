package Requests.Marketplace;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetMarketplaceConfiguration extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(612, Main.ClientMessage);
        Environment.Append(1, Main.ClientMessage);
        Environment.Append(1, Main.ClientMessage);
        Environment.Append(1, Main.ClientMessage);
        Environment.Append(5, Main.ClientMessage);
        Environment.Append(1, Main.ClientMessage);
        Environment.Append(10000, Main.ClientMessage);
        Environment.Append(48, Main.ClientMessage);
        Environment.Append(7, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
