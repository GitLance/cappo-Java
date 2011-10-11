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

public class TypingStop extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(361, Message);
        Environment.Append(Main.Data.RoomUser.VirtualId, Message);
        Environment.Append(false, Message);
        Room.SendMessage(Message);
    }
}
