package Requests.Room;

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

public class RoomListByCatsRooms extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        ServerMessage Serialize;
        int Category = Integer.parseInt(Main.DecodeString());

        if(Category == -1) Serialize = Environment.RoomListing.GetPopular();
        else Serialize = Environment.RoomListing.GetByCat(Category);

        if(Serialize!=null)
        {
            Environment.EndPacket(Main.Socket, Serialize);
        }
    }
}