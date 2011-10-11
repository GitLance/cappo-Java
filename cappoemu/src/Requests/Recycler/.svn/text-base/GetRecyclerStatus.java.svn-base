package Requests.Recycler;

import Server.Connection;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class GetRecyclerStatus extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int Status = 1;
        int TimeToWait = 0;
        if(Environment.GetTimestamp() < Main.Data.EcotronNextTime)
        {
            TimeToWait = (Main.Data.EcotronNextTime - Environment.GetTimestamp());
            Status = 3;
        }
        Environment.InitPacket(507, Main.ClientMessage);
        Environment.Append(Status, Main.ClientMessage);
        Environment.Append(TimeToWait, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
