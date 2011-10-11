package Requests.Room.Engine;

import Server.Connection;

import Server.Pet;
import Server.Room.Room;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetPetCommands extends Handler
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
            
            Environment.InitPacket(605, Main.ClientMessage);
            Environment.Append(Pet.Id, Main.ClientMessage);
            int cmds = 32;
            Environment.Append(cmds, Main.ClientMessage);
            for (int i = 0; i <= cmds; i++)
            {
                Environment.Append(i, Main.ClientMessage);
            }
            if (Pet.Level < cmds)
            {
                cmds = Pet.Level;
            }
            for (int i = 0; i < cmds; i++)
            {
                Environment.Append(i, Main.ClientMessage);
            }
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }
}
