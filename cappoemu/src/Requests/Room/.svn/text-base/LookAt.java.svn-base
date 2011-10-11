package Requests.Room;

import Server.Connection;
import Server.Room.Room;
import Server.Rotation;
import Server.ServerMessage;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class LookAt extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {

        int X = Main.DecodeInt();
        int Y = Main.DecodeInt();

        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        Main.Data.RoomUser.IdleTime = 0;

        if (X != Main.Data.RoomUser.X && Y != Main.Data.RoomUser.Y)
        {
            Main.Data.RoomUser.SetRot(Rotation.Calculate(Main.Data.RoomUser.X, Main.Data.RoomUser.Y, X, Y));
        }
    }
}
