package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class WardrobeGet extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(267, Main.ClientMessage);
        if (Main.Data.Subscription.Type > 0)
        {
            Environment.Append(true, Main.ClientMessage);
            Environment.Append(Main.Data.Wardrobes.size(), Main.ClientMessage);
            for(int Key : Main.Data.Wardrobes.keySet())
            {
                String[] Value = Main.Data.Wardrobes.get(Key).split("::");
                Environment.Append(Key, Main.ClientMessage);
                Environment.Append(Value[0], Main.ClientMessage); // Look
                Environment.Append(Value[1], Main.ClientMessage); // Gender
            }
        }
        else
        {
            Environment.Append(false, Main.ClientMessage);
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
