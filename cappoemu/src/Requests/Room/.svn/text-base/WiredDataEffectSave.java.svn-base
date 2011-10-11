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

public class WiredDataEffectSave extends Handler
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

        Room.WiredManager.SetDelay(id, Main.DecodeInt()); // Delay

        // 1=Perform the Effect on one random Furni whose type matches one of the picked Furnis
        // 2=Perform the Effect on a Furni defined by the Trigger or Condition
        // 0=Perform the Effect on picked Furnis
        int type = Main.DecodeInt();
    }
}