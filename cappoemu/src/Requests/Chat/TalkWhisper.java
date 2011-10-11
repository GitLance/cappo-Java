package Requests.Chat;

import Server.Connection;
import Server.Room.Room;
import Server.ServerMessage;
import Requests.Handler;
import Server.Player;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class TalkWhisper extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);
        if(Room == null)
        {
            return;
        }

        String Params = Main.DecodeString();
        int find = Params.indexOf(" ");
        String ToUser = Params.substring(0,find);
        String Text = Params.substring(find + 1);
        
        if(Text.length() > 100)
        {
            return;
        }

        Player User = Environment.ClientManager.GetClientByName(ToUser);

        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(25, Message);
        Environment.Append(Main.Data.RoomUser.VirtualId, Message);
        Environment.Append(Text, Message);
        Environment.Append(false, Message);

        Environment.EndPacket(Main.Socket, Message);
        Environment.EndPacket(User.Connection.Socket, Message);
    }
}
