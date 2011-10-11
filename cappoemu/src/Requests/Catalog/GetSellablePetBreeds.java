package Requests.Catalog;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetSellablePetBreeds extends Handler
{
    public static final int[] PetRaces = new int[] {25,25,12,7,4,7,13,8,13,23,1,14,10,9};

    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        String PetData = Main.DecodeString();
        int PetType = Integer.parseInt(PetData.substring(6));

        Environment.InitPacket(827, Main.ClientMessage);
        Environment.Append(PetData, Main.ClientMessage);
        Environment.Append(PetRaces[PetType], Main.ClientMessage);
        for (int i = 0; i < PetRaces[PetType]; i++)
        {
            Environment.Append(PetType, Main.ClientMessage); // pet id
            Environment.Append(i, Main.ClientMessage); // raza id
            Environment.Append(true, Main.ClientMessage); // sellable
            Environment.Append(false, Main.ClientMessage); // No se usa
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
