package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Server;
import Server.ServerMessage;

/*
 *****************
 * @author capos *
 *****************
*/

public class Move extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int MoveX = Main.DecodeInt();
        int MoveY = Main.DecodeInt();

        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        if(Main.Data.TeleporterId != 0) return; // no caminar cuando se esta usando el teleport

        Main.Data.RoomUser.IdleTime = 0;

        if (MoveX != Main.Data.RoomUser.X || MoveY != Main.Data.RoomUser.Y)
        {
            Main.Data.RoomUser.MoveTo(MoveX, MoveY, true);
        }
    }
}
