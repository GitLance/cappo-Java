package Requests.Friendlist;

import Server.Connection;
import Server.Player;
import Server.DatabaseClient;
import Requests.Handler;
import Server.Server;
import java.sql.ResultSet;

/*
 *****************
 * @author capos *
 *****************
*/

public class MessengerInit extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        DatabaseClient DB;
        try {
            DB = new DatabaseClient(Environment.DataBase);
        }
        catch (Exception ex)
        {
            Environment.Log.Print(ex);

            Main.Disconnect();
            return;
        }

        try
        {
            // Init Messenger
            Environment.InitPacket(12, Main.ClientMessage);
            Environment.Append(Main.Data.MaxFriends, Main.ClientMessage); // Limit
            Environment.Append(0, Main.ClientMessage); // no se usa
            Environment.Append(50, Main.ClientMessage); // clublimit
            Environment.Append(100, Main.ClientMessage); // viplimit
            Environment.Append(Main.Data.FriendCategories.size(), Main.ClientMessage);
            int pos = 0;
            for(String Category : Main.Data.FriendCategories)
            {
                Environment.Append(pos++, Main.ClientMessage);
                Environment.Append(Category, Main.ClientMessage);
            }
            Environment.Append(Main.Data.Friends.size(), Main.ClientMessage);
            for(int FriendId : Main.Data.Friends)
            {
                Player pClient = Environment.ClientManager.GetClient(FriendId);
                if(pClient == null) continue;

                if((pClient.Flags & Server.plrOnline) == Server.plrOnline) // Is Online?
                {
                    Environment.Append(FriendId, Main.ClientMessage);
                    Environment.Append(pClient.UserName, Main.ClientMessage);
                    Environment.Append(pClient.Sex, Main.ClientMessage);
                    Environment.Append(true, Main.ClientMessage); // Is Online
                    Environment.Append(pClient.CurrentRoom > 0, Main.ClientMessage);
                    Environment.Append(pClient.Look, Main.ClientMessage);
                    Environment.Append(0, Main.ClientMessage); // categoryId
                    Environment.Append(pClient.Motto, Main.ClientMessage);
                    Environment.Append(pClient.LastVisit, Main.ClientMessage);
                    Environment.Append(pClient.RealName, Main.ClientMessage);
                    Environment.Append("", Main.ClientMessage);
                }
                else
                {
                    ResultSet onlinedata = DB.Query("SELECT username,sex,look,mission,lastvisit,realname FROM users WHERE id = '" + FriendId + "' LIMIT 1;");

                    if(onlinedata.next())
                    {
                        Environment.Append(FriendId, Main.ClientMessage);
                        Environment.Append(onlinedata.getString("username"), Main.ClientMessage);
                        Environment.Append(onlinedata.getInt("sex"), Main.ClientMessage);
                        Environment.Append(false, Main.ClientMessage); // Is Online
                        Environment.Append(false, Main.ClientMessage); // In Room
                        Environment.Append(onlinedata.getString("look"), Main.ClientMessage);
                        Environment.Append(0, Main.ClientMessage); // categoryId
                        Environment.Append(onlinedata.getString("mission"), Main.ClientMessage);
                        Environment.Append(onlinedata.getString("lastvisit"), Main.ClientMessage);
                        Environment.Append(onlinedata.getString("realname"), Main.ClientMessage);
                        Environment.Append("", Main.ClientMessage);
                    }

                    onlinedata.close();
                }
            }
            Environment.EndPacket(Main.Socket, Main.ClientMessage);


            for(int RequesterId : Main.Data.Friend_Requests)
            {
                Player pClient = Environment.ClientManager.GetClient(RequesterId);
                if(pClient == null) continue;

                if((pClient.Flags & Server.plrOnline) == Server.plrOnline) // Is Online?
                {
                    Environment.InitPacket(132, Main.ClientMessage);
                    Environment.Append(RequesterId, Main.ClientMessage);
                    Environment.Append(pClient.UserName, Main.ClientMessage);
                    Environment.Append(Integer.toString(RequesterId), Main.ClientMessage);
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                }
                else
                {
                    ResultSet userr = DB.Query("SELECT username FROM users WHERE id = '" + RequesterId + "' LIMIT 1;");

                    if(userr.next())
                    {
                        Environment.InitPacket(132, Main.ClientMessage);
                        Environment.Append(RequesterId, Main.ClientMessage);
                        Environment.Append(userr.getString("username"), Main.ClientMessage);
                        Environment.Append(Integer.toString(RequesterId), Main.ClientMessage);
                        Environment.EndPacket(Main.Socket, Main.ClientMessage);
                    }

                    userr.close();
                }
            }

            for(int FriendId : Main.Data.Friends)
            {
                Player pClient = Environment.ClientManager.GetClient(FriendId);
                if(pClient == null) continue;

                if((pClient.Flags & Server.plrOnline) == Server.plrOnline) // Is Online?
                {
                    pClient.FriendsUpdateNeeded.add(Main.Data.Id);
                }
            }
        }
        catch (Exception ex)
        {
            Environment.Log.Print(ex);
        }

        try
        {
            DB.Close();
        }
        catch(Exception ex)
        {
            Environment.Log.Print(ex);
        }
    }
}
