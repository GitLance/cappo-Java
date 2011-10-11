package Requests.Friendlist;

import Server.Connection;
import Server.DatabaseClient;
import Server.Player;
import Requests.Handler;
import Server.Server;
import java.sql.ResultSet;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetBuddyRequests extends Handler
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

        int len = Main.Data.Friend_Requests.size();

        try
        {
            Environment.InitPacket(314, Main.ClientMessage);
            Environment.Append(len, Main.ClientMessage);
            Environment.Append(len, Main.ClientMessage);

            for(int RequesterId : Main.Data.Friend_Requests)
            {
                Player pClient = Environment.ClientManager.GetClient(RequesterId);
                if(pClient == null) continue;

                if((pClient.Flags & Server.plrOnline) == Server.plrOnline) // Is Online?
                {
                    Environment.Append(RequesterId, Main.ClientMessage);
                    Environment.Append(pClient.UserName, Main.ClientMessage);
                    Environment.Append(Integer.toString(RequesterId), Main.ClientMessage);
                }
                else
                {
                    ResultSet userr = DB.Query("SELECT username FROM users WHERE id = '" + RequesterId + "' LIMIT 1;");

                    if(userr.next())
                    {
                        Environment.Append(RequesterId, Main.ClientMessage);
                        Environment.Append(userr.getString("username"), Main.ClientMessage);
                        Environment.Append(Integer.toString(RequesterId), Main.ClientMessage);
                    }

                    userr.close();
                }
            }
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
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
