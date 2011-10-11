package Requests.Room.Engine;

import Server.Connection;
import Requests.Handler;
import Server.Room.Room;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetRoomEntryData extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if (Main.Data.LoadingRoom != 0)
        {
            Room Room = Environment.RoomManager.GetRoom(Main.Data.LoadingRoom);

            if (Room == null)
            {
                return;
            }

            if (Room.Model == null)
            {
                Main.SendNotif("Sorry, model data is missing from this room and therefore cannot be loaded.");

                Environment.InitPacket(18, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);

                Main.Data.LoadingRoom = 0;
                return;
            }
            /*
            String Hack = "xxxxxxxxxxxx"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxx000000000"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxxx00000000"
                    + (char)13
                    + "xxxxxxxxxxxx"
                    + (char)13
                    + "xxxxxxxxxxxx"
                    + (char)13;

            Environment.InitPacket(31, Main.ClientMessage);
            Environment.Append(Hack, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            
            Environment.InitPacket(470, Main.ClientMessage);
            Environment.Append(Hack, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            */
            
            String HeightMap = "";

            String[] Lines = Room.Model.Heightmap.split("\r\n");

            for(int i = 0;i<Lines.length;i++)
            {
                if (Lines[i].equals(""))
                {
                    continue;
                }

                HeightMap += Lines[i] + "\r";
            }

            Environment.InitPacket(31, Main.ClientMessage);
            Environment.Append(HeightMap, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            
/*
            String[] tmpHeightmap = Room.Model.Heightmap.split("\r");
            String ToSend = "";
            for (int y = 0; y < Room.Model.MapSizeY; y++)
            {
                if (y > 0)
                {
                    tmpHeightmap[y] = tmpHeightmap[y].substring(1);
                }
                for (int x = 0; x < Room.Model.MapSizeX; x++)
                {
                    String Square = tmpHeightmap[y].substring(x, x+1).trim().toLowerCase();
                    if (Room.Model.DoorX == x && Room.Model.DoorY == y)
                    {
                        Square = Integer.toString((int)Room.Model.DoorZ);
                    }
                    ToSend += Square;
                }
                ToSend += "\r";
            }*/
            
            Environment.InitPacket(470, Main.ClientMessage);
            Environment.Append(HeightMap, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }
}
