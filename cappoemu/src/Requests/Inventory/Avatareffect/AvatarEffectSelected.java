package Requests.Inventory.Avatareffect;

import Server.Connection;

import Server.Room.Room;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class AvatarEffectSelected extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }
        
        Main.ApplyEffect(Room, Main.DecodeInt());
    }
}
