package Requests.Room;

import Server.Connection;
import Server.ServerMessage;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomListScoreRooms extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        ServerMessage Serialize = Environment.RoomListing.GetScore();
        if(Serialize!=null)
        {
            Environment.EndPacket(Main.Socket, Serialize);
        }
    }
}