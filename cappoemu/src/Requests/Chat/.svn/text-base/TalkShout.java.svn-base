package Requests.Chat;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Server;
import Server.ServerMessage;

/*
 *****************
 * @author capos *
 *****************
*/

public class TalkShout extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        Main.Data.RoomUser.IdleTime = 0;
        
        Main.Data.RoomUser.Chat(Main,Room, Main.DecodeString(), true);
    }
}
