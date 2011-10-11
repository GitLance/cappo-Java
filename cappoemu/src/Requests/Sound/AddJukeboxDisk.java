package Requests.Sound;

import Server.Connection;
import Server.Room.Room;
import Server.TraxDisc;
import Server.TraxPlaylist.TraxSong;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class AddJukeboxDisk extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if(Room == null)
        {
            return;
        }

        int itemid = Main.DecodeInt();

        if(!Main.Data.SongInInventory.containsKey(itemid))
        {
            return;
        }

        int SongId = Main.Data.SongInInventory.get(itemid);

        Environment.InitPacket(99, Main.ClientMessage);
        Environment.Append(SongId, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
        
        TraxDisc Disc = Environment.Trax.Discs.get(SongId);

        Room.TraxPlaylist.AddSong(itemid, Disc, Main.Data.InventoryItems.get(itemid).BaseItem);
        Main.InventoryRemoveItem(itemid, false);

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
