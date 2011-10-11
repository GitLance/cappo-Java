package Requests.Users;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class ScrGetUserInfo extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if(!Main.CheckSubscription()) return;

        Environment.SerializeSubscription(Main, 1);

        // Extend Subscription Promo
        /*Environment.InitPacket(630,true);
        if(Main.Subscription.Type == 2)
        {
            Environment.AppendInt(4898); // id
            Environment.AppendStringWithBreak("HABBO_CLUB_VIP_1_MONTH");
            Environment.AppendInt(25); // precio
            Environment.Append(false); // upgrade or buy
            Environment.Append(true); // vip
            Environment.AppendInt(1); // meses
            Environment.AppendInt(31); // dias de subscripcion
            Environment.AppendInt(2030); // año del final
            Environment.AppendInt(3); // mes del final
            Environment.AppendInt(31); // dia del final

            Environment.AppendInt(45); // regular price
            Environment.AppendInt(30); // days left for this offer
        }
        else
        {
            Environment.AppendInt(4896); // id
            Environment.AppendStringWithBreak("HABBO_CLUB_BASIC_1_MONTH");
            Environment.AppendInt(10); // precio
            Environment.Append(false); // upgrade or buy
            Environment.Append(false); // vip
            Environment.AppendInt(1); // meses
            Environment.AppendInt(31); // dias de subscripcion
            Environment.AppendInt(2011); // año del final
            Environment.AppendInt(3); // mes del final
            Environment.AppendInt(31); // dia del final

            Environment.AppendInt(15); // regular price
            Environment.AppendInt(30); // days left for this offer
        }
        Environment.EndPacket();
        Main.SendData();*/
    }
}
