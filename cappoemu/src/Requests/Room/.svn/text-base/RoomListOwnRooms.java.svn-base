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

public class RoomListOwnRooms extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(451, Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage);
        Environment.Append(Integer.toString(5), Main.ClientMessage);
        Room[] Rooms = new Room[Main.Data.OwnRooms.size()];
        int pos = 0;
        for(int RoomId : Main.Data.OwnRooms)
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