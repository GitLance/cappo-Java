package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class WardrobeSave extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int SlotId = Main.DecodeInt();

        String SelectedLook = Main.DecodeString();

        if (!Environment.ValidateLook(SelectedLook))
        {
            Main.Disconnect();
            return;
        }

        String LookGender = Main.DecodeString();

        Main.Data.Wardrobes.put(SlotId, SelectedLook+"::"+LookGender);
    }
}
