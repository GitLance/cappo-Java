package Requests.Tracking;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class EventLog extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        String Type = Main.DecodeString();
        String Item = Main.DecodeString();
        String Event = Main.DecodeString();
        String Data = Main.DecodeString();

        //Environment.Log.Print("Stats: Type="+Type+" Item="+Item+" Event="+Event+" Datat="+Data);
    }
}
