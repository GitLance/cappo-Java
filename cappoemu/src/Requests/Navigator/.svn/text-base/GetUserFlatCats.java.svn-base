package Requests.Navigator;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetUserFlatCats extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int Count = Environment.NavigatorCategories.CategoriesCount();

        Environment.InitPacket(221, Main.ClientMessage);
        Environment.Append(Count, Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage);
        Environment.Append("No Category", Main.ClientMessage);
        for(int i = 1;i<Count;i++)
        {
            Environment.Append(true, Main.ClientMessage);
            Environment.Append(i, Main.ClientMessage);
            Environment.Append(Environment.NavigatorCategories.GetCategory(i), Main.ClientMessage);
        }
        Environment.Append("", Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
