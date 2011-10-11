package Requests.Room.Session;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class OpenFlatConnection extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int RoomId = Main.DecodeInt();
        String Password = Main.DecodeString();

        Main.LoadRoom(RoomId, Password);
    }
}
