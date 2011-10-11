package Requests.Sound;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetUserSongDisks extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(333, Main.ClientMessage);
        Environment.Append(Main.Data.SongInInventory.size(), Main.ClientMessage);
        for(int Key : Main.Data.SongInInventory.keySet())
        {
            int Value = Main.Data.SongInInventory.get(Key);
            Environment.Append(Key, Main.ClientMessage);
            Environment.Append(Value, Main.ClientMessage);
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
