package Requests.Users;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetMOTD extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Main.SendNotif(Environment.MOTD, 0);
    }
}
