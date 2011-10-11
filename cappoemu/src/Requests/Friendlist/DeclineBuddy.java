package Requests.Friendlist;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class DeclineBuddy extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int Action = Main.DecodeInt();

        if(Action==1)
        {
            int UserId = Main.DecodeInt();

            int index = Main.Data.Friend_Requests.indexOf(UserId);

            if(index < 0)
            {
                return;
            }

            Main.Data.Friend_Requests.remove(index);
        }
        else
        {
            Main.Data.Friend_Requests.clear();
        }
    }
}
