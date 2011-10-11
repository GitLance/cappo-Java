package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Room.RoomItem;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class MoveWallItem extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }
        
        if (!Room.CheckRights(Main.Data, false))
        {
            return;
        }

        RoomItem Item = Room.GetWallItem(Main.DecodeInt());

        if (Item == null)
        {
            return;
        }

        String[] DataBits = Main.DecodeString().split(" ");

        if (!DataBits[2].equals("l") && !DataBits[2].equals("r"))
        {
            Environment.InitPacket(516, Main.ClientMessage);
            Environment.Append(11, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        String[] widD = DataBits[0].substring(3).split(",");
        int widthX = Integer.parseInt(widD[0]);
        int widthY = Integer.parseInt(widD[1]);
        if (widthX < 0 || widthY < 0 || widthX > 200 || widthY > 200)
        {
            Environment.InitPacket(516, Main.ClientMessage);
            Environment.Append(11, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        String[] lenD = DataBits[1].substring(2).split(",");
        int lengthX = Integer.parseInt(lenD[0]);
        int lengthY = Integer.parseInt(lenD[1]);
        if (lengthX < 0 || lengthY < 0 || lengthX > 200 || lengthY > 200)
        {
            Environment.InitPacket(516, Main.ClientMessage);
            Environment.Append(11, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            return;
        }

        Item.WallPos = ":w=" + widthX + "," + widthY + " " + "l=" + lengthX + "," + lengthY + " " + DataBits[2];
        Room.SetWallItem(Main, Item, false);
    }
}
