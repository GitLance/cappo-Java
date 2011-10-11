package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Server;
import java.util.Map;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomPopularTags extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Map<String, Integer> PopularTags = Environment.RoomManager.GetTags();

        Environment.InitPacket(452, Main.ClientMessage);
        Environment.Append(PopularTags.size(), Main.ClientMessage);
        for(String Tag : PopularTags.keySet())
        {
            Environment.Append(Tag, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
