package Requests.Room;

import Server.Connection;
import Server.Room.Room;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomCreate extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if(Main.Data.OwnRooms.size() >= Main.Data.MaxRooms)
        {
            return; // lammer..
        }

        Room NewRoom = Environment.RoomManager.CreateRoom(Main, Main.DecodeString(), Main.DecodeString());

        if (NewRoom != null)
        {
            Environment.InitPacket(59, Main.ClientMessage);
            Environment.Append(NewRoom.Id, Main.ClientMessage);
            Environment.Append(NewRoom.Name, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }

}
