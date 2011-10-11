package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class ResetUnseenItems extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int Category = Main.DecodeInt();

        // pending..
    }
}
