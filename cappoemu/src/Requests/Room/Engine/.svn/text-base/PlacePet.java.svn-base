package Requests.Room.Engine;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Pet;
import Server.Room.RoomBot;
import Server.Room.RoomUser;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class PlacePet extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);
        
        if (Room == null)
        {
            return;
        }
        
        if((Room.Flags & Server.rmAllowPets) != Server.rmAllowPets && !Room.CheckRights(Main.Data, true))
        {
            Environment.InitPacket(608, Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        if (Room.PetCounter >= 5)
        {
            Environment.InitPacket(608, Main.ClientMessage);
            Environment.Append(2, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }
        
        int PetId = Main.DecodeInt();

        Pet Pet = Main.GetPet(PetId);

        if (Pet == null)
        {
            return;
        }

        int X = Main.DecodeInt();
        int Y = Main.DecodeInt();

        if (!Room.CanWalk2(X, Y, true))
        {
            Environment.InitPacket(608, Main.ClientMessage);
            Environment.Append(4, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        RoomBot Bot = new RoomBot(Pet.Id, Pet.Name, "", Pet.Type + " " + Pet.Race + " " + Pet.Color);
        Double Z = Room.SqAbsoluteHeight(X, Y, false, null);
        RoomUser PetUser = Room.DeployPet(Bot, Pet, X, Y, Z);

        if (PetUser == null)
        {
            return;
        }

        Environment.InitPacket(604, Main.ClientMessage);
        Environment.Append(Pet.Id, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
        
        Main.Data.InventoryPets.remove(Pet.Id);
    }
}
