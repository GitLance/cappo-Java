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

public class UserInfo extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if (!Main.Data.IsStaff())
        {
            return;
        }

        int UserId = Main.DecodeInt();

        Player pClient = Environment.ClientManager.GetClient(UserId);
        if(pClient == null) return;

        if((pClient.Flags & Server.plrOnline) == Server.plrOnline) // Is Online?
        {
            Environment.InitPacket(533, Main.ClientMessage);

            Environment.Append(pClient.Id, Main.ClientMessage);
            Environment.Append(pClient.UserName, Main.ClientMessage);

            // pending..
            Environment.Append(0, Main.ClientMessage); // (Environment.GetTimestamp() - reg_timestamp) / 60
            Environment.Append(0, Main.ClientMessage); // (Environment.GetTimestamp() - login_timestamp) / 60

            Environment.Append(true, Main.ClientMessage); // online

            // pending..
            Environment.Append(0, Main.ClientMessage); // cfhs
            Environment.Append(0, Main.ClientMessage); // abusive cfhs
            Environment.Append(0, Main.ClientMessage); // cautions
            Environment.Append(0, Main.ClientMessage); // bans

            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }
}
