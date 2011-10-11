package Requests.Room.Avatar;

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

public class Dance extends Handler
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

        int DanceId = Main.DecodeInt();

        if(DanceId != 1)
        {
            if(DanceId < 0)
            {
                DanceId = 0;
            }
            else if(DanceId > 4 || (Main.Data.Subscription.Type > 0 && !Main.CheckSubscription()))
            {
                DanceId = 0;
            }
        }

        if(DanceId > 0 && Main.Data.RoomUser.CarryItemID > 0)
        {
            Main.Data.RoomUser.CarryItem(Room, 0);
        }

        Main.Data.RoomUser.DanceId = DanceId;

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(480, Message);
        Environment.Append(Main.Data.RoomUser.VirtualId, Message);
        Environment.Append(DanceId, Message);
        Room.SendMessage(Message);
    }
}
