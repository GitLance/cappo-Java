package Requests.Room;

import Server.Connection;
import Server.Room.Room;
import Server.Room.RoomEvent;
import Server.ServerMessage;
import Requests.Handler;

import Server.Server;
import java.util.ArrayList;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomEventStart extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null || !Room.CheckRights(Main.Data, true) || Room.Event != null || Room.State != 0)
        {
            return;
        }

        int category = Main.DecodeInt();
        String name = Main.DecodeString();
        String description = Main.DecodeString();

        Room.Event = new RoomEvent(Room.Id, name, description, category, null, Environment.GetTimestamp());
        Room.Event.Tags = new ArrayList<String>();

        int tagCount = Main.DecodeInt();
        for (int i = 0; i < tagCount; i++)
        {
            Room.Event.Tags.add(Main.DecodeString());
        }

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(370, Message);
        Environment.Append(Integer.toString(Main.Data.Id), Message);
        Environment.Append(Main.Data.UserName, Message);
        Environment.Append(Integer.toString(Room.Id), Message);
        Environment.Append(Room.Event.Category, Message);
        Environment.Append(Room.Event.Name, Message);
        Environment.Append(Room.Event.Description, Message);
        Environment.Append(Room.Event.StartTime, Message);
        Environment.Append(Room.Event.Tags.size(), Message);
        for(String Tag : Room.Event.Tags)
        {
            Environment.Append(Tag, Message);
        }
        Room.SendMessage(Message);
    }
}
