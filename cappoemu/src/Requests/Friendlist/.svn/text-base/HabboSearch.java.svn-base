package Requests.Friendlist;

import Server.Connection;
import Server.Player;
import Server.DatabaseClient;
import Requests.Handler;
import Server.Server;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/*
 *****************
 * @author capos *
 *****************
*/

public class HabboSearch extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        String Search = Main.DecodeString();

        List<Integer> Results = new ArrayList<Integer>();
        int size = 0;

        Object[] keys = Environment.ClientManager.GetClients().values().toArray();
        for (int a = 0; a < keys.length; a++)
        {
            Player Current = (Player)keys[a];
            if(Current==null) continue;
            if(Results.contains(Current.Id))
            {
                continue;
            }

            if(!Current.UserName.contains(Search) && !Current.Tags.contains(Search))
            {
                continue;
            }

            Results.add(Current.Id);

            if(++size >= 20)
            {
                break;
            }
        }

        if(size < 20) // find more results in mysql..
        {
            DatabaseClient DB;
            ResultSet table;

            try
            {
                DB = new DatabaseClient(Environment.DataBase);
            }
            catch (Exception ex)
            {
                return;
            }

            
            try
            {
                String tmp = "%"+Search+"%";
                table = DB.SecureQuery("SELECT id FROM users WHERE username LIKE ? ORDER BY username DESC LIMIT "+(20-size)+";",tmp);
                while (table.next())
                {
                    int id = table.getInt("id");
                    if(!Results.contains(id))
                    {
                        Results.add(id);
                    }
                }
            }
            catch (Exception ex)  { }

            DB.Close();
        }

        Environment.InitPacket(435, Main.ClientMessage);
        Player[] PlayersFriends = new Player[Results.size()];
        Player[] Players = new Player[Results.size()];

        int friendcount = 0;
        int normalcount = 0;
        for(int UserId : Results)
        {
            try
            {
                Player User = Environment.ClientManager.GetClient(UserId);
                if (User == null) continue;

                if(Main.Data.Friends.contains(UserId))
                {
                    PlayersFriends[friendcount++] = User;
                }
                else
                {
                    Players[normalcount++] = User;
                }
            }
            catch(Exception ex)
            {
                Environment.Log.Print(ex);
            }
        }

        Environment.Append(friendcount, Main.ClientMessage);
        for(int i=0;i<friendcount;i++)
        {
            Environment.Append(PlayersFriends[i].Id, Main.ClientMessage);
            Environment.Append(PlayersFriends[i].UserName, Main.ClientMessage);
            Environment.Append(PlayersFriends[i].Motto, Main.ClientMessage);
            Environment.Append((PlayersFriends[i].Flags & Server.plrOnline) == Server.plrOnline, Main.ClientMessage);
            Environment.Append(PlayersFriends[i].CurrentRoom > 0, Main.ClientMessage);
            Environment.Append("", Main.ClientMessage); // Not Used
            Environment.Append(0, Main.ClientMessage); // categoryId - Not Used
            Environment.Append(PlayersFriends[i].Look, Main.ClientMessage);
            Environment.Append(PlayersFriends[i].LastVisit, Main.ClientMessage);
            Environment.Append(PlayersFriends[i].RealName, Main.ClientMessage);
        }

        Environment.Append(normalcount, Main.ClientMessage);
        for(int i=0;i<normalcount;i++)
        {
            Environment.Append(Players[i].Id, Main.ClientMessage);
            Environment.Append(Players[i].UserName, Main.ClientMessage);
            Environment.Append(Players[i].Motto, Main.ClientMessage);
            Environment.Append((Players[i].Flags & Server.plrOnline) == Server.plrOnline, Main.ClientMessage); // Is Online
            Environment.Append(Players[i].CurrentRoom > 0, Main.ClientMessage);
            Environment.Append("", Main.ClientMessage); // Not Used
            Environment.Append(0, Main.ClientMessage); // categoryId - Not Used
            Environment.Append(Players[i].Look, Main.ClientMessage);
            Environment.Append(Players[i].LastVisit, Main.ClientMessage);
            Environment.Append(Players[i].RealName, Main.ClientMessage);
        }

        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
