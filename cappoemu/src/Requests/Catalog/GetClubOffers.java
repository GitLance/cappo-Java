package Requests.Catalog;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetClubOffers extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(625, Main.ClientMessage);
        Environment.Append(4, Main.ClientMessage);

        Environment.Append(4896, Main.ClientMessage); // id
        Environment.Append("HABBO_CLUB_BASIC_1_MONTH", Main.ClientMessage);
        Environment.Append(15, Main.ClientMessage); // precio
        Environment.Append(false, Main.ClientMessage); // upgrade or buy
        Environment.Append(false, Main.ClientMessage); // vip
        Environment.Append(1, Main.ClientMessage); // meses
        Environment.Append(31, Main.ClientMessage); // dias de subscripcion
        Environment.Append(2011, Main.ClientMessage); // año del final
        Environment.Append(3, Main.ClientMessage); // mes del final
        Environment.Append(31, Main.ClientMessage); // dia del final

        Environment.Append(4897, Main.ClientMessage); // id
        Environment.Append("HABBO_CLUB_BASIC_3_MONTHS", Main.ClientMessage);
        Environment.Append(25, Main.ClientMessage); // precio
        Environment.Append(false, Main.ClientMessage); // upgrade or buy
        Environment.Append(false, Main.ClientMessage); // vip
        Environment.Append(3, Main.ClientMessage); // meses
        Environment.Append(93, Main.ClientMessage); // dias de subscripcion
        Environment.Append(2011, Main.ClientMessage); // año del final
        Environment.Append(3, Main.ClientMessage); // mes del final
        Environment.Append(31, Main.ClientMessage); // dia del final

        Environment.Append(4898, Main.ClientMessage); // id
        Environment.Append("HABBO_CLUB_VIP_1_MONTH", Main.ClientMessage);
        Environment.Append(45, Main.ClientMessage); // precio
        Environment.Append(false, Main.ClientMessage); // upgrade or buy
        Environment.Append(true, Main.ClientMessage); // vip
        Environment.Append(1, Main.ClientMessage); // meses
        Environment.Append(31, Main.ClientMessage); // dias de subscripcion
        Environment.Append(2030, Main.ClientMessage); // año del final
        Environment.Append(3, Main.ClientMessage); // mes del final
        Environment.Append(31, Main.ClientMessage); // dia del final

        Environment.Append(4899, Main.ClientMessage); // id
        Environment.Append("HABBO_CLUB_VIP_3_MONTHS", Main.ClientMessage);
        Environment.Append(65, Main.ClientMessage); // precio
        Environment.Append(false, Main.ClientMessage); // upgrade or buy
        Environment.Append(true, Main.ClientMessage); // vip
        Environment.Append(3, Main.ClientMessage); // meses
        Environment.Append(93, Main.ClientMessage); // dias de subscripcion
        Environment.Append(2011, Main.ClientMessage); // año del final
        Environment.Append(3, Main.ClientMessage); // mes del final
        Environment.Append(31, Main.ClientMessage); // dia del final

        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
