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

public class AcceptBuddy extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int Count = Main.DecodeInt();
        for(int i = 0;i<Count;i++)
        {
            int UserId = Main.DecodeInt();

            int index = Main.Data.Friend_Requests.indexOf(UserId);

            if(index < 0)
            {
                return;
            }
            
            Main.Data.Friend_Requests.remove(index);

            Player Client = Environment.ClientManager.GetClient(UserId);
            if(Client == null) return;

            Main.Data.Friends.add(UserId);
            Main.Data.FriendsUpdateNeeded.add(UserId);

            Client.Friends.add(Main.Data.Id);
            Client.FriendsUpdateNeeded.add(Main.Data.Id);
        }
    }
}
