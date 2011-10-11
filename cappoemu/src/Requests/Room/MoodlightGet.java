package Requests.Room;

import Server.Connection;
import Server.MoodlightData.MoodlightPreset;
import Server.Room.Room;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class MoodlightGet extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null || !Room.CheckRights(Main.Data, true) || Room.MoodlightData == null)
        {
            return;
        }

        Environment.InitPacket(365, Main.ClientMessage);
        Environment.Append(Room.MoodlightData.Presets.size(), Main.ClientMessage);
        Environment.Append(Room.MoodlightData.CurrentPreset, Main.ClientMessage);
        int i = 0;
        for(MoodlightPreset Preset : Room.MoodlightData.Presets)
        {
            Environment.Append(++i, Main.ClientMessage);
            Environment.Append(Preset.BackgroundOnly?2:1, Main.ClientMessage);
            Environment.Append(Preset.ColorCode, Main.ClientMessage);
            Environment.Append(Preset.ColorIntensity, Main.ClientMessage);
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
