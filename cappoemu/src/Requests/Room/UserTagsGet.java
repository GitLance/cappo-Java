package Requests.Room;

import Server.Connection;
import Server.Player;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class UserTagsGet extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Player pClient = Environment.ClientManager.GetClient(Main.DecodeInt());
        if(pClient == null) return;

        if((pClient.Flags & Server.plrOnline) == Server.plrOnline) // Is Online?
        {
            Environment.InitPacket(350, Main.ClientMessage);
            Environment.Append(pClient.Id, Main.ClientMessage);
            Environment.Append(pClient.Tags.size(), Main.ClientMessage);
            for(String Tag : pClient.Tags)
            {
                Environment.Append(Tag, Main.ClientMessage);
            }
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }
}
