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

public class RequestBuddy extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if(Main.Data.Friends.size()+Main.Data.Friend_Requests.size() >= Main.Data.MaxFriends)
        {
            Environment.InitPacket(260, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        String UserName = Main.DecodeString();

        Player To = Environment.ClientManager.GetClientByName(UserName);
        if(To == null)
        {
            return;
        }

        if(To.Friend_Requests.contains(Main.Data.Id) || To.Friends.contains(Main.Data.Id))
        {
            return;
        }

        int HaveRequest = Main.Data.Friend_Requests.indexOf(To.Id);

        if(HaveRequest >= 0) // If have request of the New Request
        {
            Main.Data.Friend_Requests.remove(HaveRequest);

            Main.Data.Friends.add(To.Id);
            Main.Data.FriendsUpdateNeeded.add(To.Id);

            To.Friends.add(Main.Data.Id);
            To.FriendsUpdateNeeded.add(Main.Data.Id);
            return;
        }

        if(To.Friends.size()+To.Friend_Requests.size() >= To.MaxFriends)
        {
            Environment.InitPacket(260, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
            Environment.Append(2, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        To.Friend_Requests.add(Main.Data.Id);

        if((To.Flags & Server.plrOnline) == Server.plrOnline)
        {
            ServerMessage Message = new ServerMessage();
            Environment.InitPacket(132, Message);
            Environment.Append(Main.Data.Id, Message);
            Environment.Append(Main.Data.UserName, Message);
            Environment.Append(Integer.toString(Main.Data.Id), Message);
            Environment.EndPacket(To.Connection.Socket, Message);
        }
    }
}
