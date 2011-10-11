package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GroupBadgesGet extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(309, Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
