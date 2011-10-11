package Requests.Sound;

import Server.Connection;
import Server.Room.Room;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetNowPlaying extends Handler
{

    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if(Room == null)
        {
            return;
        }

        if(Room.TraxPlaylist.Playing)
        {
            Environment.InitPacket(327, Main.ClientMessage);
            Environment.Append(Room.TraxPlaylist.CurrentSong.SongId, Main.ClientMessage);
            Environment.Append(Room.TraxPlaylist.SongIndex, Main.ClientMessage);
            Environment.Append(Room.TraxPlaylist.CurrentSong.SongId, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
            Environment.Append((Room.TraxPlaylist.CurrentItemTrax.TaskSteps / 2) * 1000, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }

}
