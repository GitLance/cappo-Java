package Requests.Sound;

import Server.Connection;

import Server.TraxDisc;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetSongInfo extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int Requests = Main.DecodeInt();
        Environment.InitPacket(300, Main.ClientMessage);
        Environment.Append(Requests, Main.ClientMessage);
        for(int i = 0;i<Requests;i++)
        {
            TraxDisc Disc = Environment.Trax.Discs.get(Main.DecodeInt());
            if(Disc != null)
            {
                Environment.Append(Disc.Id, Main.ClientMessage);
                Environment.Append(Disc.Name, Main.ClientMessage);
                Environment.Append(Disc.SongData, Main.ClientMessage);
                Environment.Append(Disc.Length, Main.ClientMessage);
                Environment.Append(Disc.Author, Main.ClientMessage);
            }
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
