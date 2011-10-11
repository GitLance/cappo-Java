package Requests.Room.Action;

import Server.Connection;
import Server.Player;
import Server.Room.Room;
import Server.ServerMessage;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class AssignRights extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null || !Room.CheckRights(Main.Data, true))
        {
            return;
        }
        
        Player Client = Environment.ClientManager.GetClient(Main.DecodeInt());

        if(Client == null || (Client.Flags & Server.plrOnline) != Server.plrOnline)
        {
            return;
        }

        if(Client.CurrentRoom != Room.Id)
        {
            return;
        }

        if (!Room.UsersWithRights.contains(Client.Id))
        {
            Room.UsersWithRights.add(Client.Id);
        }

        Environment.InitPacket(FlatControllerAdded, Main.ClientMessage);
        Environment.Append(Room.Id, Main.ClientMessage);
        Environment.Append(Client.Id, Main.ClientMessage);
        Environment.Append(Client.UserName, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        Client.RoomUser.HavePowers = true;
        Client.RoomUser.UpdateNeeded = true;
        
        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(YouAreController, Message);
        Environment.EndPacket(Client.Connection.Socket, Message);
    }
}
