package Requests.Staff;

import Server.Connection;
import Server.Player;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class BanUser extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if(Main.Data.RankLevel < 5)
        {
            return;
        }

        int UserId = Main.DecodeInt();
        String Message = Main.DecodeString();
        int Hours = Main.DecodeInt();

        int Type = 1;

        if(Hours == 100000)
        {
            Type = 10; // perm
        }

        Player Client = Environment.ClientManager.GetClient(UserId);
        if(Client == null) return;
        
        if((Client.Flags & Server.plrOnline) == Server.plrOnline) // Is Online?
        {
            Environment.InitPacket(287, Main.ClientMessage);
            Environment.Append(Type, Main.ClientMessage);
            Environment.EndPacket(Client.Connection.Socket, Main.ClientMessage);
            
            Client.Connection.Disconnect();
        }

        // pending..
        // Add ban..
    }
}
