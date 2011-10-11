package Requests.Staff;

import Server.Connection;
import Server.Room.Room;
import Requests.Handler;
import Server.Player;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class RoomTools extends Handler
{

    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if (!Main.Data.IsStaff())
        {
            return;
        }

        int RoomId = Main.DecodeInt();

        Room Room = Environment.RoomManager.GetRoom(RoomId);
        if (Room == null) return;

        Environment.InitPacket(538, Main.ClientMessage);
        Environment.Append(Room.Id, Main.ClientMessage);
        Environment.Append(Room.UsersNow, Main.ClientMessage);

        boolean OwnerInRoom = false;
        Player Owner = Room.Environment.ClientManager.GetClient(Room.OwnerId);
        if(Owner != null)
        {
            if((Owner.Flags & Server.plrOnline) == Server.plrOnline) // Is Online?
            {
                OwnerInRoom = (Owner.CurrentRoom == Room.Id);
            }
        }
        Environment.Append(OwnerInRoom, Main.ClientMessage);
        Environment.Append(Room.OwnerId, Main.ClientMessage);
        Environment.Append(Room.Owner, Main.ClientMessage);
        Environment.Append(Room.Id, Main.ClientMessage);
        Environment.Append(Room.Name, Main.ClientMessage);
        Environment.Append(Room.Description, Main.ClientMessage);
        Environment.Append(Room.Tags.size(), Main.ClientMessage);
        for(String Tag : Room.Tags)
        {
            Environment.Append(Tag, Main.ClientMessage);
        }
        if (Room.Event != null)
        {
            Environment.Append(true, Main.ClientMessage);
            Environment.Append(Room.Event.Name, Main.ClientMessage);
            Environment.Append(Room.Event.Description, Main.ClientMessage);
            Environment.Append(Room.Event.Tags.size(), Main.ClientMessage);
            for(String Tag : Room.Event.Tags)
            {
                Environment.Append(Tag, Main.ClientMessage);
            }
        }
        else
        {
            Environment.Append(false, Main.ClientMessage);
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
