package Requests.Poll;

import Server.Connection;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class PollReject extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int PollId = Main.DecodeInt();

    }
}
