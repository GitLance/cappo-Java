package Requests.Navigator;

import Server.Connection;
import Requests.Handler;
import Server.Server;
import java.util.ArrayList;
import java.util.Collection;

/*
 *****************
 * @author capos *
 *****************
*/

public class DeleteFavouriteRoom extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int RoomId = Main.DecodeInt();

        Collection<Integer> l = new ArrayList<Integer>();
        l.add(RoomId);
        Main.Data.Favorite_Rooms.removeAll(l);

        Environment.InitPacket(459, Main.ClientMessage);
        Environment.Append(RoomId, Main.ClientMessage);
        Environment.Append(false, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}