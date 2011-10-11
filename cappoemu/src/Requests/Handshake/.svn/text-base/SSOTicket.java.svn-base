package Requests.Handshake;

import Server.AvatarEffect;
import Server.Connection;
import Server.DatabaseClient;
import Server.Room.Room;
import Requests.Handler;
import Server.Server;
import java.sql.ResultSet;

/*
 *****************
 * @author capos *
 *****************
*/

public class SSOTicket extends Handler
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
            Environment.InitPacket(33, Main.ClientMessage);
            Environment.Append(-400, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        String ticket = Main.DecodeString();

        try
        {
            ResultSet userdata = DB.SecureQuery("SELECT id FROM users WHERE auth_ticket = ? LIMIT 1;", ticket);
            
            if(!userdata.next())
            {
                userdata.close();
                DB.Close();

                Environment.InitPacket(33, Main.ClientMessage);
                Environment.Append(-3, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
                return;
            }

            int Id = userdata.getInt("id");

            userdata.close();

            Main.Data = Environment.ClientManager.GetClient(Id);

            if(Main.Data == null)
            {
                DB.Close();

                Environment.InitPacket(33, Main.ClientMessage);
                Environment.Append(-400, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
                return;
            }

            if((Main.Data.Flags & Server.plrOnline) == Server.plrOnline) // Is Online? // Kick the user connected..
            {
                Main.Data.Id = 0; // No Update User in Mysql

                // 1 banned
                // 2 concurrentlogin
                // 10 banned
                // other logout
                Environment.InitPacket(287, Main.Data.Connection.ClientMessage);
                Environment.Append(2, Main.Data.Connection.ClientMessage);
                Environment.EndPacket(Main.Data.Connection.Socket, Main.Data.Connection.ClientMessage);
                
                Main.Data.Connection.Disconnect();

                Thread.sleep(200);

                Main.Data.Id = Id;
            }
            else
            {
                for(int RoomId : Main.Data.OwnRooms)
                {
                    Room Room = Environment.RoomManager.GetRoom(RoomId);
                    if (Room == null)
                    {
                        Room = Environment.RoomManager.LoadRoom(RoomId);
                    }
                }
            }

            ResultSet table = DB.Query("SELECT * FROM items WHERE user_id = "+Id+" AND room_id = 0;");
            while (table.next())
            {
                Main.AddItem(table.getInt("id"), table.getString("extra_data"), Environment.Furniture.Items.get(table.getInt("base_item")));
            }
            table.close();

            DB.Update("UPDATE `users` SET `online`=1,`auth_ticket`='' WHERE `id` = '"+Main.Data.Id+"' LIMIT 1;");

            DB.Close();

            Main.Data.Connection = Main;
            Environment.ClientManager.SetOnline(true);

            Main.Data.SetFlag(Server.plrOnline,true);
            Main.Data.SetFlag(Server.plrPongOk,true);

            // Authentication OK
            Environment.InitPacket(3, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);

            Environment.InitPacket(460, Main.ClientMessage);
            Environment.Append(Main.Data.Effects.size(), Main.ClientMessage);
            for(AvatarEffect Effect : Main.Data.Effects)
            {
                Environment.Append(Effect.EffectId, Main.ClientMessage);
                Environment.Append(Effect.TotalDuration, Main.ClientMessage);
                Environment.Append(!Effect.Activated, Main.ClientMessage);
                int diff = Environment.GetTimestamp() - Effect.StampActivated;
                Environment.Append(Effect.TotalDuration - diff, Main.ClientMessage);
            }
            Environment.EndPacket(Main.Socket, Main.ClientMessage);

            // Enter Home
            Environment.InitPacket(455, Main.ClientMessage);
            Environment.Append(Main.Data.HomeRoom, Main.ClientMessage);// Room id
            Environment.Append(Main.Data.HomeRoom, Main.ClientMessage);// Room id
            Environment.EndPacket(Main.Socket, Main.ClientMessage);

            // Favorite Rooms
            Environment.InitPacket(458, Main.ClientMessage);
            Environment.Append(30, Main.ClientMessage); // Limit
            Environment.Append(Main.Data.Favorite_Rooms.size(), Main.ClientMessage);
            for(int RoomId : Main.Data.Favorite_Rooms)
            {
                Environment.Append(RoomId, Main.ClientMessage);
            }
            Environment.EndPacket(Main.Socket, Main.ClientMessage);

            Environment.InitPacket(2, Main.ClientMessage);
            Environment.Append(Main.Data.Subscription.Type, Main.ClientMessage);
            Environment.Append(Main.Data.RankLevel, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);

            Environment.InitPacket(290, Main.ClientMessage);
            Environment.Append(true, Main.ClientMessage); // Not Used
            Environment.Append(true, Main.ClientMessage); // Trade enabled/disabled
            Environment.EndPacket(Main.Socket, Main.ClientMessage);

            // HabboNotifications enable
            Environment.InitPacket(517, Main.ClientMessage);
            Environment.Append(true, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);

            if(Main.Data.IsStaff()) // Mod Tool
            {
                Environment.InitPacket(531, Main.ClientMessage);
                
                Environment.Append(0, Main.ClientMessage); // count issues

                {
                    
                }

                Environment.Append(3, Main.ClientMessage); // Count MessagesToUser Templates

                {
                    Environment.Append("mensaje predeterminado 1", Main.ClientMessage);

                    Environment.Append("mensaje predeterminado 2", Main.ClientMessage);

                    Environment.Append("mensaje predeterminado 3", Main.ClientMessage);
                }

                Environment.Append(1, Main.ClientMessage); // Count Offences Categories

                {
                    Environment.Append("Acoso Sexual", Main.ClientMessage);

                    Environment.Append(2, Main.ClientMessage); // Count Offences

                    Environment.Append("Me habbo violo", Main.ClientMessage);
                    Environment.Append("Tonterias que se mandaran...", Main.ClientMessage);

                    Environment.Append("Pidio Sexo", Main.ClientMessage);
                    Environment.Append("Tonterias que se mandaran...", Main.ClientMessage);
                }

                for(int i = 0;i<Main.Data.RankLevel;i++)
                {
                    Environment.Append(true, Main.ClientMessage);
                }

                Environment.Append(4, Main.ClientMessage); // Count Messages Templates

                {
                    Environment.Append("Test template 1", Main.ClientMessage);

                    Environment.Append("Test template 2", Main.ClientMessage);

                    Environment.Append("Test template 3", Main.ClientMessage);

                    Environment.Append("Test template 4", Main.ClientMessage);
                }

                Environment.EndPacket(Main.Socket, Main.ClientMessage);
            }

            Environment.InitPacket(628, Main.ClientMessage);
            Environment.Append(2, Main.ClientMessage); // Count
            Environment.Append(0, Main.ClientMessage); // Pixels
            Environment.Append(Main.Data.Pixels, Main.ClientMessage); // Pixels Ammount
            Environment.Append(4, Main.ClientMessage); // Shells
            Environment.Append(Main.Data.Shells, Main.ClientMessage); // Shells Ammount
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            
            Main.GiveBadge("Z64");
            
            if(Main.Data.IsStaff())
            {
                Main.GiveBadge("ADM");
            }
        }
        catch (Exception ex)
        {
            Environment.Log.Print(ex);
            
            Main.Data = null;

            Environment.InitPacket(33, Main.ClientMessage);
            Environment.Append(-400, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
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
