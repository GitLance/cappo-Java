package Requests.Sound;

import Server.Connection;
import Server.Room.Room;
import Server.TraxPlaylist.TraxSong;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetJukeboxPlayList extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);
        
        if(Room == null)
        {
            return;
        }

        Environment.InitPacket(334, Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage);
        Environment.Append(Room.TraxPlaylist.PlaylistByIndex.size(), Main.ClientMessage);
        int count = 0;
        for(TraxSong Song: Room.TraxPlaylist.PlaylistByIndex)
        {
            Environment.Append(count++, Main.ClientMessage);
            Environment.Append(Song.SongId, Main.ClientMessage);
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
