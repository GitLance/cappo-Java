package Server.ItemInteractor;

import Server.Connection;
import Server.Room.Room;
import Server.Room.RoomItem;
import Server.ServerMessage;

/*
 *****************
 * @author capos *
 *****************
*/

public class InteractorTrax extends Interactor {

    @Override
    public void OnPlace(Connection User, RoomItem Item) { }

    @Override
    public void OnRemove(Connection User, RoomItem Item) { }

    @Override
    public void OnTrigger(Connection User, RoomItem Item, int Request, boolean UserHasRights)
    {
        if (!UserHasRights)
        {
            return;
        }

        if(Request == -1)
        {
            return;
        }
                
        Room Room = User.Environment.RoomManager.GetRoom(Item.RoomId);
        
        if (Room == null)
        {
            return;
        }

        if(!Room.TraxPlaylist.Playing)
        {
            Room.TraxPlaylist.StartPlaying();
            if(Room.TraxPlaylist.CurrentSong != null)
            {
                Room.TraxPlaylist.CurrentItemTrax = Item;
                Room.TraxPlaylist.Playing = true;

                Item.ExtraData = "1";
                Item.UpdateNeeded = true;

                Item.TaskSteps = (Room.TraxPlaylist.CurrentSong.Length / 1000) * 2;
                Item.ActiveTask = 21;

                ServerMessage NotifySong = new ServerMessage();
                User.Environment.InitPacket(327, NotifySong);
                User.Environment.Append(Room.TraxPlaylist.CurrentSong.SongId, NotifySong);
                User.Environment.Append(Room.TraxPlaylist.SongIndex, NotifySong);
                User.Environment.Append(Room.TraxPlaylist.CurrentSong.SongId, NotifySong);
                User.Environment.Append(0, NotifySong);
                User.Environment.Append(0, NotifySong);
                Room.SendMessage(NotifySong);
            }
        }
        else
        {
            Item.ExtraData = "0";
            Item.UpdateNeeded = true;

            Item.ActiveTask = 0;
            Room.TraxPlaylist.Playing = false;
            Room.TraxPlaylist.CurrentItemTrax = null;
            
            ServerMessage NotifySong = new ServerMessage();
            User.Environment.InitPacket(327, NotifySong);
            User.Environment.Append(-1, NotifySong);
            User.Environment.Append(-1, NotifySong);
            User.Environment.Append(-1, NotifySong);
            User.Environment.Append(-1, NotifySong);
            User.Environment.Append(0, NotifySong);
            Room.SendMessage(NotifySong);
        }
    }
}
