package Requests.Room.Pets;

import Server.Connection;
import Requests.Handler;


import Server.Room.Room;
import Server.Pet;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RespectPet extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);
        if (Room == null)
        {
            return;
        }

        int UserId = Room.GetRoomPetById(Main.DecodeInt());
        if (UserId != -1)
        {
            Pet Pet = Room.UserList[UserId].PetData;
            Pet.GiveRespect(Room);
            Pet.GiveExperience(10, Room, Room.UserList[UserId]);
            Main.Data.DailyPetRespectPoints--;
        }
    }
}