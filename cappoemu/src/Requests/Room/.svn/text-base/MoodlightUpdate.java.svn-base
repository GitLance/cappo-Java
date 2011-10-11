package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Room.RoomItem;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class MoodlightUpdate extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null || !Room.CheckRights(Main.Data, true) || Room.MoodlightData == null)
        {
            return;
        }


        int Preset = Main.DecodeInt();
        int BackgroundMode = Main.DecodeInt();
        String ColorCode = Main.DecodeString();
        int Intensity = Main.DecodeInt();


        Room.MoodlightData.Enabled = true;
        Room.MoodlightData.CurrentPreset = Preset;
        Room.MoodlightData.UpdatePreset(ColorCode, Intensity, (BackgroundMode>1));

        RoomItem Item = Room.GetWallItem(Room.MoodlightData.ItemId);

        Item.ExtraData = Room.MoodlightData.GenerateExtraData();
        Item.UpdateNeeded = true;
    }
}
