package Requests.Room;

import Server.Connection;
import Server.Player;
import Server.Room.Room;
import Requests.Handler;
import Server.Server;
import java.util.ArrayList;
import java.util.List;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomListFriendsInRooms extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(451, Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage);
        Environment.Append(Integer.toString(4), Main.ClientMessage);
        List<Room> roomList = new ArrayList<Room>();
        for(int FriendId : Main.Data.Friends)
        {
            Player pClient = Environment.ClientManager.GetClient(FriendId);
            if(pClient == null) continue;

            if((pClient.Flags & Server.plrOnline) == Server.plrOnline) // Is Online?
            {
                Room Room = Environment.RoomManager.GetRoom(pClient.CurrentRoom);
                if (Room == null) continue;
                
                roomList.add(Room);
            }
        }
        Environment.Append(roomList.size(), Main.ClientMessage);
        for(Room Room : roomList)
        {
            if(Room!=null)
            {
                Room.Serialize(Main.ClientMessage);
            }
        }
        Environment.Append(false, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}