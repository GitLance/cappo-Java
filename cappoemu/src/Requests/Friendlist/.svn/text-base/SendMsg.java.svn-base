package Requests.Friendlist;

import Server.Connection;
import Server.Player;
import Server.ServerMessage;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class SendMsg extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int UserId = Main.DecodeInt();

        if(!Main.Data.Friends.contains(UserId))
        {
            Environment.InitPacket(261, Main.ClientMessage);
            Environment.Append(6, Main.ClientMessage);
            Environment.Append(UserId, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        Player Client = Environment.ClientManager.GetClient(UserId);
        if(Client == null) return;
        
        if((Client.Flags & Server.plrOnline) != Server.plrOnline)
        {
            Environment.InitPacket(261, Main.ClientMessage);
            Environment.Append(5, Main.ClientMessage);
            Environment.Append(UserId, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(134, Message);
        Environment.Append(Main.Data.Id, Message);
        Environment.Append(Main.DecodeString(), Message);
        Environment.EndPacket(Client.Connection.Socket, Message);
    }
}
