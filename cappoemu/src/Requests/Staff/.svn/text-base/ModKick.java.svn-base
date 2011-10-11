package Requests.Staff;

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

public class ModKick extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if(Main.Data.RankLevel < 4)
        {
            return;
        }
        
        int UserId = Main.DecodeInt();

        Player Client = Environment.ClientManager.GetClient(UserId);
        if(Client == null) return;

        if((Client.Flags & Server.plrOnline) != Server.plrOnline) // Is Online?
        {
            return;
        }

        Room Room = Environment.RoomManager.GetRoom(Client.CurrentRoom);
        if (Room == null) return;

        Room.RemoveUserFromRoom(Client.Connection, true, false, false);

        String Message = Main.DecodeString();
        Client.Connection.SendNotif(Message, 1);
    }
}
