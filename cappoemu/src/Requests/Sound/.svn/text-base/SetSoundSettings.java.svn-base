package Requests.Sound;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class SetSoundSettings extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Main.Data.Volumen = Main.DecodeInt();
    }
}
