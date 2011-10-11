package Server;

import Server.Room.RoomUser;
import Server.Room.Room;

/*
 *****************
 * @author capos *
 *****************
*/

public abstract class BotAI
{
    public abstract void OnUserSay(Room Room, RoomUser PetUser, RoomUser User, String Message);
    public abstract void OnTimerTick(Room Room, RoomUser PetUser);
    public abstract void OnUserLeaveRoom(Room Room, RoomUser PetUser, Connection Client);
    public abstract void OnUserEnterRoom(Room Room, RoomUser PetUser, RoomUser User);
    public abstract void OnSelfLeaveRoom(Room Room, RoomUser PetUser, boolean Kicked);
    public abstract void OnSelfEnterRoom(Room Room, RoomUser PetUser);
}
