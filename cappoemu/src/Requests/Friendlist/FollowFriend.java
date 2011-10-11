package Requests.Friendlist;

import Server.Connection;
import Server.Player;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class FollowFriend extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int UserId = Main.DecodeInt();

        if(!Main.Data.Friends.contains(UserId))
        {
            Environment.InitPacket(349, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        Player Client = Environment.ClientManager.GetClient(UserId);

        if(Client == null) // offline
        {
            Environment.InitPacket(349, Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        if(Client.CurrentRoom==0)
        {
            Environment.InitPacket(349, Main.ClientMessage);
            Environment.Append(2, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        Main.LoadRoom(Client.CurrentRoom, "");
    }
}
