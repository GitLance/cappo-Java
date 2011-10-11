package Requests.Friendlist;

import Server.Connection;
import Server.Player;
import Server.ServerMessage;
import Requests.Handler;
import Server.Server;
import java.util.ArrayList;
import java.util.List;

/*
 *****************
 * @author capos *
 *****************
*/

public class SendRoomInvite extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        List<Integer> SendTo = new ArrayList<Integer>();
        List<Integer> Failed = new ArrayList<Integer>();

        int count = Main.DecodeInt();

        for (int i = 0; i < count; i++)
        {
            SendTo.add(Main.DecodeInt());
        }

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(135, Message);
        Environment.Append(Main.Data.Id, Message);
        Environment.Append(Main.DecodeString(), Message);

        for(int UserId : SendTo)
        {
            Player Client = Environment.ClientManager.GetClient(UserId);
            if(Client == null)
            {
                Failed.add(UserId);
                continue;
            }
            if((Client.Flags & Server.plrOnline) != Server.plrOnline)
            {
                Failed.add(UserId);
                continue;
            }
            
            Environment.EndPacket(Client.Connection.Socket, Message);
        }

        if(!Failed.isEmpty())
        {
            Environment.InitPacket(262, Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage);
            Environment.Append(Failed.size(), Main.ClientMessage);
            for(int UserId : Failed)
            {
                Environment.Append(UserId, Main.ClientMessage);
            }
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }
}
