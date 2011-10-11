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

public class RoomEventEdit extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null || !Room.CheckRights(Main.Data, true) || Room.Event == null)
        {
            return;
        }

        Room.Event.Category = Main.DecodeInt();
        Room.Event.Name = Main.DecodeString();
        Room.Event.Description = Main.DecodeString();
        Room.Event.Tags.clear();

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
