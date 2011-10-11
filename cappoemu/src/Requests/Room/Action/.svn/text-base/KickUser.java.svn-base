package Requests.Room.Action;

import Server.Connection;
import Server.Player;
import Server.Room.Room;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class KickUser extends Handler
{

    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int UserId = Main.DecodeInt();

        Player Client = Environment.ClientManager.GetClient(UserId);
        if(Client == null) return;

        if((Client.Flags & Server.plrOnline) != Server.plrOnline) // Is Online?
        {
            return;
        }

        Room Room = Environment.RoomManager.GetRoom(Client.CurrentRoom);

        if (Room == null || !Room.CheckRights(Main.Data, true))
        {
            return;
        }

        Room.RemoveUserFromRoom(Client.Connection, true, false, false);

        Client.Connection.SendNotif("Kicked!", 1);
    }

}
