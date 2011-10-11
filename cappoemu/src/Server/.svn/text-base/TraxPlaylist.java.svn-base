package Server;

import Server.Room.RoomItem;
import Server.Furniture.Item;
import java.util.ArrayList;
import java.util.List;

/*
 *****************
 * @author capos *
 *****************
*/

public class TraxPlaylist
{
    public List<TraxSong> PlaylistByIndex = new ArrayList<TraxSong>();

    public boolean Playing;
    public int SongIndex;
    public TraxSong CurrentSong;
    public RoomItem CurrentItemTrax;
    
    public class TraxSong
    {
        public int SongId = 0;
        public Item BaseItem = null;
        public int Length;
        public int ItemId;
    }

    public void AddSong(int ItemId, TraxDisc Disc, Item BaseItem)
    {
        TraxSong Current = new TraxSong();
        Current.BaseItem = BaseItem;
        Current.SongId = Disc.Id;
        Current.Length = Disc.Length;
        Current.ItemId = ItemId;
        PlaylistByIndex.add(Current);
    }
    
    public void StartPlaying()
    {
        if(PlaylistByIndex.isEmpty())
        {
            CurrentSong = null;
        }
        else
        {
            CurrentSong = PlaylistByIndex.get(SongIndex = 0);
        }
    }

    public void NextSong()
    {
        SongIndex = ++SongIndex % PlaylistByIndex.size();
        CurrentSong = PlaylistByIndex.get(SongIndex);
        
        if(CurrentSong == null)
        {
            Playing = false;
        }
    }
}
