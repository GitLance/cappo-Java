package Requests.Room;

import Server.Connection;
import Server.DatabaseClient;
import Server.Room.Room;
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

public class RoomTextSearch extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        String Search = Main.DecodeString();
        
        List<Integer> Results = new ArrayList<Integer>();
        Object[] keys = Environment.RoomManager.GetRooms().values().toArray();
        for (int a = 0; a < keys.length; a++)
        {
            Room Current = (Room) keys[a];
            if(Current==null) continue;
            if(Results.contains(Current.Id))
            {
                continue;
            }
            
            if(!Current.Name.contains(Search) && !Current.Tags.contains(Search) && !Current.Owner.contains(Search))
            {
                continue;
            }

            Results.add(Current.Id);

            if(Results.size() >= 20)
            {
                break;
            }
        }

        if(Results.size() < 20) // find more results in mysql..
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
                table = DB.SecureQuery("SELECT id FROM rooms WHERE caption LIKE ? OR tags LIKE ? OR owner LIKE ? ORDER BY caption DESC LIMIT "+(20-Results.size())+";",tmp,tmp,tmp);
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
        
        Environment.InitPacket(451, Main.ClientMessage);
        Environment.Append(1, Main.ClientMessage);
        Environment.Append(Integer.toString(9), Main.ClientMessage);
       // Environment.Append(Search, Main.ClientMessage);
        Room[] Rooms = new Room[Results.size()];
        int pos = 0;
        for(int RoomId : Results)
        {
            try
            {
                Room Room = Environment.RoomManager.GetRoom(RoomId);
                if (Room == null)
                {
                    Room = Environment.RoomManager.LoadRoom(RoomId);
                    if (Room == null) continue;
                }
                Rooms[pos++] = Room;
            }
            catch(Exception ex)
            {
                Environment.Log.Print(ex);
            }
        }
        Environment.Append(pos, Main.ClientMessage);
        for(int i=0;i<pos;i++)
        {
            Rooms[i].Serialize(Main.ClientMessage);
        }
        Environment.Append(false, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
