package Requests.Handshake;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GenerateSecretKey extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(1, Main.ClientMessage);
        Environment.Append(Main.Crypto.getPublicKey(), Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        String CkEy = Main.DecodeString();
        
        System.out.println(CkEy);
        
        Main.Crypto.InitRc4(Main.Crypto.HextoBytes(Main.Crypto.generateSharedKey(CkEy)));
    }
}
