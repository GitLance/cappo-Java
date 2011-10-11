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

public class GetPetInfo extends Handler
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

            Environment.InitPacket(601, Main.ClientMessage);
            Environment.Append(Pet.Id, Main.ClientMessage);
            Environment.Append(Pet.Name, Main.ClientMessage);
            Environment.Append(Pet.Level, Main.ClientMessage);
            Environment.Append(Pet.MaxLevel, Main.ClientMessage);
            Environment.Append(Pet.Experience, Main.ClientMessage);
            Environment.Append(Pet.ExperienceLevels[Pet.Level], Main.ClientMessage);
            Environment.Append(Pet.Energy, Main.ClientMessage);
            Environment.Append(Pet.MaxEnergyLevels[Pet.Level], Main.ClientMessage);
            Environment.Append(Pet.Nutrition, Main.ClientMessage);
            Environment.Append(Pet.MaxNutritionLevels[Pet.Level], Main.ClientMessage);
            Environment.Append(Pet.Type, Main.ClientMessage);
            Environment.Append(Pet.Race, Main.ClientMessage);
            Environment.Append(Pet.Color, Main.ClientMessage);
            Environment.Append(Pet.Respects, Main.ClientMessage);
            Environment.Append(Pet.OwnerId, Main.ClientMessage);
            Environment.Append((Environment.GetTimestamp() - Pet.TimeCreated) / 86400, Main.ClientMessage);
            Environment.Append(Pet.OwnerName, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }
}
