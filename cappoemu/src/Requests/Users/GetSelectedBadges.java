package Requests.Users;

import Server.Badge;
import Server.Connection;
import Server.Player;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetSelectedBadges extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int userId = Main.DecodeInt();
        Player pClient = Environment.ClientManager.GetClient(userId);
        if(pClient==null) return;
        
        if((pClient.Flags & Server.plrOnline) == Server.plrOnline) // Is Online?
        {
            Environment.InitPacket(228, Main.ClientMessage);
            Environment.Append(pClient.Id, Main.ClientMessage);
            Environment.Append(pClient.BadgesSelected.size(), Main.ClientMessage);
            for(Badge Badge : pClient.BadgesSelected)
            {
                Environment.Append(Badge.Slot, Main.ClientMessage);
                Environment.Append(Badge.Code, Main.ClientMessage);
            }
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }
}
