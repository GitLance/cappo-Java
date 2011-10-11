package Requests.Staff;

import Server.Connection;
import Server.Player;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class SendMessage extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if (!Main.Data.IsStaff())
        {
            return;
        }

        int UserId = Main.DecodeInt();
        String Message = Main.DecodeString();

        Player Client = Environment.ClientManager.GetClient(UserId);
        if (Client == null) return;

        if((Client.Flags & Server.plrOnline) != Server.plrOnline) // Is Online?
        {
            return;
        }

        Client.Connection.SendNotif(Message, 1);
    }
}
