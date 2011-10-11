package Requests.Inventory.Pets;

import Server.Connection;
import Requests.Handler;
import Server.Pet;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetPetInventory extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(600, Main.ClientMessage);
        Environment.Append(Main.Data.InventoryPets.size(),Main.ClientMessage);
        for(Pet Pet : Main.Data.InventoryPets.values())
        {
            Pet.SerializeInventory(Environment,Main.ClientMessage);
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
        
    }
}
