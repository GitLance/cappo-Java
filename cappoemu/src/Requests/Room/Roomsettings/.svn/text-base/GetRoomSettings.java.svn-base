package Requests.Room.Roomsettings;

import Server.Connection;
import Server.DatabaseClient;
import Requests.Handler;
import Server.Room.Room;
import Server.Server;
import java.sql.ResultSet;


/*
 *****************
 * @author capos *
 *****************
*/

public class GetRoomSettings extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }
        
        if (!Room.CheckRights(Main.Data, true))
        {
            return;
        }

        Environment.InitPacket(465, Main.ClientMessage);
        Environment.Append(Room.Id, Main.ClientMessage);
        Environment.Append(Room.Name, Main.ClientMessage);
        Environment.Append(Room.Description, Main.ClientMessage);
        Environment.Append(Room.State, Main.ClientMessage);
        Environment.Append(Room.Category, Main.ClientMessage);
        Environment.Append(Room.UsersMax, Main.ClientMessage);
        Environment.Append(25, Main.ClientMessage);
        Environment.Append(Room.Tags.size(), Main.ClientMessage);
        for(String Tag : Room.Tags)
        {
            Environment.Append(Tag, Main.ClientMessage);
        }

        int count = Room.UsersWithRights.size();
        Environment.Append(count, Main.ClientMessage);
        if(count > 0)
        {
            DatabaseClient DB;
            try {
                DB = new DatabaseClient(Environment.DataBase);
            }
            catch (Exception ex)
            {
                Environment.Log.Print(ex);
                return;
            }

            for(int UserId : Room.UsersWithRights)
            {
                try
                {
                    ResultSet userr = DB.Query("SELECT username FROM users WHERE id = '" + UserId + "' LIMIT 1;");
                    if (userr.next())
                    {
                        Environment.Append(UserId, Main.ClientMessage);
                        Environment.Append(userr.getString("username"), Main.ClientMessage);
                    }
                    userr.close();
                }
                catch (Exception ex)
                {
                    Environment.Log.Print(ex);
                }
            }

            DB.Close();
        }

        Environment.Append(count, Main.ClientMessage);

        Environment.Append((Room.Flags & Server.rmAllowPets) == Server.rmAllowPets, Main.ClientMessage);
        Environment.Append((Room.Flags & Server.rmAllowPetsEat) == Server.rmAllowPetsEat, Main.ClientMessage);
        Environment.Append((Room.Flags & Server.rmAllowWalkthrough) == Server.rmAllowWalkthrough, Main.ClientMessage);
        Environment.Append((Room.Flags & Server.rmHideWall) == Server.rmHideWall, Main.ClientMessage);

        Environment.Append(Room.WallAnchor, Main.ClientMessage);
        Environment.Append(Room.FloorAnchor, Main.ClientMessage);

        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
