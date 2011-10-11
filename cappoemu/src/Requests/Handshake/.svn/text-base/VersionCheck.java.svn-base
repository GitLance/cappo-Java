package Requests.Handshake;


import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class VersionCheck extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        /*int num = */Main.DecodeInt(); // always 401
        String ClientPath = Main.DecodeString();
        String ExternalsUrl = Main.DecodeString();
    }
}
