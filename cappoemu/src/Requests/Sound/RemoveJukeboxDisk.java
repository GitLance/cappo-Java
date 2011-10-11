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

public class RemoveJukeboxDisk extends Handler {

    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if(Room == null)
        {
            return;
        }

        int index = Main.DecodeInt();
        TraxSong Song2 = Room.TraxPlaylist.PlaylistByIndex.get(index);
        Main.AddItem(Song2.ItemId, Integer.toString(Song2.SongId), Song2.BaseItem, true);
        Room.TraxPlaylist.PlaylistByIndex.remove(index);
        
        Environment.InitPacket(333, Main.ClientMessage);
        Environment.Append(Main.Data.SongInInventory.size(), Main.ClientMessage);
        for(int Key : Main.Data.SongInInventory.keySet())
        {
            int Value = Main.Data.SongInInventory.get(Key);
            Environment.Append(Key, Main.ClientMessage);
            Environment.Append(Value, Main.ClientMessage);
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

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
