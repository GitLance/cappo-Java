package Requests.Room.Engine;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RemovePetFromFlat extends Handler
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
            Main.Data.InventoryPets.put(Room.UserList[UserId].Id, Room.UserList[UserId].PetData);

            Environment.InitPacket(603, Main.ClientMessage);
            Room.UserList[UserId].PetData.SerializeInventory(Environment, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);

            Room.RemovePet(UserId, false);
        }
    }
}