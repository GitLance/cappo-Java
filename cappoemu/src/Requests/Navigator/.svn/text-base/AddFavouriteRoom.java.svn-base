package Requests.Navigator;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class AddFavouriteRoom extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int RoomId = Main.DecodeInt();

        int FavCount = Main.Data.Favorite_Rooms.size();

        if (FavCount >= 30 || Main.Data.Favorite_Rooms.contains(RoomId))
        {
            Environment.InitPacket(33, Main.ClientMessage);
            Environment.Append(-9001, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);

            return;
        }

        Main.Data.Favorite_Rooms.add(RoomId);

        Environment.InitPacket(459, Main.ClientMessage);
        Environment.Append(RoomId, Main.ClientMessage);
        Environment.Append(true, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}



