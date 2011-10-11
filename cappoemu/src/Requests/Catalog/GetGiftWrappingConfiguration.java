package Requests.Catalog;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetGiftWrappingConfiguration extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(620, Main.ClientMessage);

        Environment.Append(true, Main.ClientMessage); // gift wrapping Enabled?

        Environment.Append(1, Main.ClientMessage); // gift wrapping Cost

        Environment.Append(10, Main.ClientMessage);
        for(int i = 0;i<10;i++)
            Environment.Append(i+3372, Main.ClientMessage);

        Environment.Append(7, Main.ClientMessage);
        for(int i = 0;i<7;i++)
            Environment.Append(i, Main.ClientMessage);

        Environment.Append(11, Main.ClientMessage);
        for(int i = 0;i<11;i++)
            Environment.Append(i, Main.ClientMessage);

        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
