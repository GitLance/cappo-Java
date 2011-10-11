package Requests.Room;

import Server.Connection;
import Server.Room.Room;
import Server.Room.RoomItem;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class WiredDataConditionSave extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        RoomItem Item = Room.GetItem(Main.DecodeInt());

        if (Item == null)
        {
            return;
        }

        int id = Room.WiredManager.GetIdWired(Item);
        if (id == 9999)
        {
            return;
        }

        int count = Main.DecodeInt(); // options
        if(count > 0)
        {
            if(Room.WiredManager.WiredOptions[id]==null || Room.WiredManager.WiredOptions[id].length != count)
            {
                Room.WiredManager.WiredOptions[id] = new int[count];
            }
            for(int i=0;i<count;i++)
            {
                Room.WiredManager.WiredOptions[id][i] = Main.DecodeInt();
            }
        }

        Room.WiredManager.WiredData[id] = Main.DecodeString();

        count = Main.DecodeInt(); // Childs Count
        for(int i=0;i<count;i++)
        {
            Room.WiredManager.AddChild(id, i, Room.GetItem(Main.DecodeInt())); // Furni id
        }

        Main.DecodeInt();
    }
}
