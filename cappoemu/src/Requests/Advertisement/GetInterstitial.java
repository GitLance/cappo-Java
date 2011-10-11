package Requests.Advertisement;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetInterstitial extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(258, Main.ClientMessage);
        Environment.Append("http://www.opera.com/bitmaps/company/education/wsc_728x90.jpg", Main.ClientMessage);
        Environment.Append("http://google.com", Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
