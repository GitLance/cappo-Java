package Requests.Room;

import Server.Connection;
import Server.Room.Room;
import Server.ServerMessage;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class UserRespect extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);
        if (Room == null)
        {
            return;
        }

        int UserId = Room.GetRoomPetById(Main.DecodeInt());
        if (UserId != -1)
        {
            ServerMessage Message = new ServerMessage();
            Environment.InitPacket(440, Message);
            Environment.Append(Room.UserList[UserId].Client.Data.Id, Message);
            Environment.Append(++Room.UserList[UserId].Client.Data.Respects, Message);
            Room.SendMessage(Message);
            
            Main.Data.DailyRespectPoints--;
        }
    }
}
