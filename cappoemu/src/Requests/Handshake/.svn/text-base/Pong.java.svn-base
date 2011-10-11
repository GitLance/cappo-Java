package Requests.Handshake;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class Pong extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Main.Data.SetFlag(Server.plrPongOk,true);
    }
}
