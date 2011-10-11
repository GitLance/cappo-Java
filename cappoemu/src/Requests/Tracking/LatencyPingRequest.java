package Requests.Tracking;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class LatencyPingRequest extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int param = Main.DecodeInt();

        Environment.InitPacket(354, Main.ClientMessage);
        Environment.Append(param, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        if (Main.PixelsNeedsUpdate())
        {
            Main.GivePixels(100);
        }
        Main.EffectsCheckExpired();
    }
}
