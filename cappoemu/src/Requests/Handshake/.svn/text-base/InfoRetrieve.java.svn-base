package Requests.Handshake;

import Server.Connection;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class InfoRetrieve extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(5, Main.ClientMessage);
        Environment.Append(Main.Data.Id, Main.ClientMessage);
        Environment.Append(Main.Data.UserName, Main.ClientMessage);
        Environment.Append(Main.Data.Look, Main.ClientMessage);
        Environment.Append(Main.Data.Sex==1 ? "M" : "F", Main.ClientMessage);
        Environment.Append(Main.Data.Motto, Main.ClientMessage);
        Environment.Append(Main.Data.RealName, Main.ClientMessage);
        Environment.Append(false, Main.ClientMessage); // not used
        Environment.Append(Main.Data.Respects, Main.ClientMessage);
        Environment.Append(Main.Data.DailyRespectPoints, Main.ClientMessage);
        Environment.Append(Main.Data.DailyPetRespectPoints, Main.ClientMessage);
        Environment.Append(false, Main.ClientMessage); // Stream events allowed, pending..!
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
