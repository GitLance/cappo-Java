package Requests.Inventory.Badges;

import Server.Badge;
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

public class SetActivatedBadges extends Handler
{
    private static final int SLOTS = 5;

    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        Main.Data.BadgesSelected.clear();

        for(int i = 0;i<SLOTS;i++)
        {
            int Slot = Main.DecodeInt();

            Badge badge = Main.GetBadge(Main.DecodeString());
            if(badge!=null)
            {
                badge.Slot = Slot;
                Main.Data.BadgesSelected.add(badge);
            }
        }

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(228, Message);
        Environment.Append(Main.Data.Id, Message);
        Environment.Append(Main.Data.BadgesSelected.size(), Message);
        for(Badge Badge : Main.Data.BadgesSelected)
        {
            Environment.Append(Badge.Slot, Message);
            Environment.Append(Badge.Code, Message);
        }
        Room.SendMessage(Message);
    }
}
