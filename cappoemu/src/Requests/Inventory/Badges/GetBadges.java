package Requests.Inventory.Badges;

import Server.Badge;
import Server.Connection;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class GetBadges extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(229, Main.ClientMessage);
        Environment.Append(Main.Data.Badges.size(), Main.ClientMessage);
        for(Badge Badge : Main.Data.Badges)
        {
            Environment.Append(Badge.Slot, Main.ClientMessage);
            Environment.Append(Badge.Code, Main.ClientMessage);
        }
        Environment.Append(Main.Data.BadgesSelected.size(), Main.ClientMessage);
        for(Badge Badge : Main.Data.BadgesSelected)
        {
            Environment.Append(Badge.Slot, Main.ClientMessage); // ya no se usa..
            Environment.Append(Badge.Code, Main.ClientMessage);
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
