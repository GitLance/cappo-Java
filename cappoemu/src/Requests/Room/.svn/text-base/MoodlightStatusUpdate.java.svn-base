package Requests.Room;

import Server.Connection;
import Server.Room.Room;
import Server.Room.RoomItem;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class MoodlightStatusUpdate extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null || !Room.CheckRights(Main.Data, true) || Room.MoodlightData == null)
        {
            return;
        }

        Room.MoodlightData.Enabled = !(Room.MoodlightData.Enabled);

        RoomItem Item = Room.GetWallItem(Room.MoodlightData.ItemId);

        Item.ExtraData = Room.MoodlightData.GenerateExtraData();
        Item.UpdateNeeded = true;
    }
}
