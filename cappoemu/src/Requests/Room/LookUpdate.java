package Requests.Room;

import Server.Connection;
import Server.Room.Room;
import Server.ServerMessage;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class LookUpdate extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        String Gender = Main.DecodeString();
        String SelectedLook = Main.DecodeString();

        if (!Environment.ValidateLook(SelectedLook))
        {
            Main.Disconnect();
            return;
        }

        Main.Data.Look = SelectedLook;
        Main.Data.Sex = Gender.equalsIgnoreCase("M") ? 1 : 0;

        Environment.InitPacket(266, Main.ClientMessage);
        Environment.Append(-1, Main.ClientMessage);
        Environment.Append(Main.Data.Look, Main.ClientMessage);
        Environment.Append(Main.Data.Sex==1 ? "M" : "F", Main.ClientMessage);
        Environment.Append(Main.Data.Motto, Main.ClientMessage);
        Environment.Append(Main.Data.AchievementsScore, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
        
        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(266, Message);
        Environment.Append(Main.Data.RoomUser.VirtualId, Message);
        Environment.Append(Main.Data.Look, Message);
        Environment.Append(Main.Data.Sex==1 ? "M" : "F", Message);
        Environment.Append(Main.Data.Motto, Message);
        Environment.Append(Main.Data.AchievementsScore, Message);
        Room.SendMessage(Message);
    }
}
