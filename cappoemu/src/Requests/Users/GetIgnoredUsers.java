package Requests.Users;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetIgnoredUsers extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        // Pending..
        
        Environment.InitPacket(420, Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage); // cantidad
        //Environment.AppendStringWithBreak("USERNAME")
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
