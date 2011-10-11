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

public class FriendListUpdate extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(13, Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage); // categories..
        Environment.Append(Main.Data.FriendsUpdateNeeded.size(), Main.ClientMessage);
        for(int id : Main.Data.FriendsUpdateNeeded)
        {
            Player pClient = Environment.ClientManager.GetClient(id);
            if(pClient == null) continue;

            if((pClient.Flags & Server.plrOnline) == Server.plrOnline) // Is Online?
            {
                Environment.Append(false, Main.ClientMessage);
                Environment.Append(pClient.Id, Main.ClientMessage);
                Environment.Append(pClient.UserName, Main.ClientMessage);
                Environment.Append(pClient.Sex, Main.ClientMessage);
                Environment.Append(true, Main.ClientMessage); // ONLINE
                Environment.Append(pClient.CurrentRoom > 0, Main.ClientMessage);
                Environment.Append(pClient.Look, Main.ClientMessage);
                Environment.Append(0, Main.ClientMessage); // categoryId
                Environment.Append(pClient.Motto, Main.ClientMessage);
                Environment.Append(pClient.LastVisit, Main.ClientMessage);
                Environment.Append(pClient.RealName, Main.ClientMessage);
                Environment.Append("", Main.ClientMessage);
            }
        }
        Main.Data.FriendsUpdateNeeded.clear();
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
