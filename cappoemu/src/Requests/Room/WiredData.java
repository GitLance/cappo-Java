package Requests.Room;

import Server.Connection;
import Server.Room.Room;
import Server.Room.RoomItem;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class WiredData extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        RoomItem Item = Room.GetItem(Main.DecodeInt());

        if (Item == null)
        {
            return;
        }

        int id = Room.WiredManager.GetIdWired(Item);
        if (id == 9999)
        {
            return;
        }

        RoomItem[] WiredChilds = Room.WiredManager.GetWiredChilds(id);
        int len = 0;


        if(Item.BaseItem.Interaction == Environment.Furniture.WIRED_TRIGGER)
        {
            Environment.InitPacket(650, Main.ClientMessage);
            Environment.Append(false, Main.ClientMessage); // activa el checkbox (The triggering furni type matches the type of one of the picked furnis)
            Environment.Append(Room.WiredManager.MAX_CHILDS_PER_WIRED, Main.ClientMessage); // Maximo de furnis
            for (int i = 0; i < WiredChilds.length; i++)
            {
                if (WiredChilds[i] == null) continue;

                len++;
            }
            Environment.Append(len, Main.ClientMessage); // cantidad de items seleccionados
            for (int i = 0; i < WiredChilds.length; i++)
            {
                if (WiredChilds[i] == null) continue;

                Environment.Append(WiredChilds[i].Id, Main.ClientMessage); // Item id
            }
            Environment.Append(Item.BaseItem.SpriteId, Main.ClientMessage); // sprite id wired
            Environment.Append(Item.Id, Main.ClientMessage);// Wired Item id
            Environment.Append(Room.WiredManager.WiredData[id], Main.ClientMessage);

            if(Room.WiredManager.WiredOptions[id]==null)
            {
                Environment.Append(0, Main.ClientMessage);
            }
            else
            {
                Environment.Append(Room.WiredManager.WiredOptions[id].length, Main.ClientMessage);
                for(int option : Room.WiredManager.WiredOptions[id])
                {
                    Environment.Append(option, Main.ClientMessage);
                }
            }

            Environment.Append(0, Main.ClientMessage); // selectiontype

            Environment.Append(Item.BaseItem.WiredType, Main.ClientMessage); // Trigger type
            Environment.Append(0, Main.ClientMessage); // conflict count
            // Environment.AppendInt(13); // conflict itemid
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
        else if(Item.BaseItem.Interaction == Environment.Furniture.WIRED_EFFECT)
        {
            Environment.InitPacket(651, Main.ClientMessage);
            Environment.Append(false, Main.ClientMessage);
            Environment.Append(Room.WiredManager.MAX_CHILDS_PER_WIRED, Main.ClientMessage); // Maximo de furnis
            for (int i = 0; i < WiredChilds.length; i++)
            {
                if (WiredChilds[i] == null) continue;

                len++;
            }
            Environment.Append(len, Main.ClientMessage); // cantidad de items seleccionados
            for (int i = 0; i < WiredChilds.length; i++)
            {
                if (WiredChilds[i] == null) continue;

                Environment.Append(WiredChilds[i].Id, Main.ClientMessage); // Item id
            }
            Environment.Append(Item.BaseItem.SpriteId, Main.ClientMessage); // sprite id wired
            Environment.Append(Item.Id, Main.ClientMessage);// Wired Item id
            Environment.Append(Room.WiredManager.WiredData[id], Main.ClientMessage);

            if(Room.WiredManager.WiredOptions[id]==null)
            {
                Environment.Append(0, Main.ClientMessage);
            }
            else
            {
                Environment.Append(Room.WiredManager.WiredOptions[id].length, Main.ClientMessage);
                for(int option : Room.WiredManager.WiredOptions[id])
                {
                    Environment.Append(option, Main.ClientMessage);
                }
            }

            Environment.Append(0, Main.ClientMessage); // selectiontype

            Environment.Append(Item.BaseItem.WiredType, Main.ClientMessage); // effect type
            Environment.Append(Room.WiredManager.EffectDelay[id], Main.ClientMessage); // delay (1 = 0.5 sec, etc..)
            Environment.Append(0, Main.ClientMessage); // conflict count
            // Environment.AppendInt(13); // conflict itemid
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
        else if(Item.BaseItem.Interaction == Environment.Furniture.WIRED_CONDITION)
        {
            Environment.InitPacket(652, Main.ClientMessage);
            Environment.Append(false, Main.ClientMessage);
            Environment.Append(Room.WiredManager.MAX_CHILDS_PER_WIRED, Main.ClientMessage); // Maximo de furnis
            for (int i = 0; i < WiredChilds.length; i++)
            {
                if (WiredChilds[i] == null) continue;

                len++;
            }
            Environment.Append(len, Main.ClientMessage); // cantidad de items seleccionados
            for (int i = 0; i < WiredChilds.length; i++)
            {
                if (WiredChilds[i] == null) continue;

                Environment.Append(WiredChilds[i].Id, Main.ClientMessage); // Item id
            }
            Environment.Append(Item.BaseItem.SpriteId, Main.ClientMessage); // sprite id wired
            Environment.Append(Item.Id, Main.ClientMessage);// Wired Item id
            Environment.Append(Room.WiredManager.WiredData[id], Main.ClientMessage);

            if(Room.WiredManager.WiredOptions[id]==null)
            {
                Environment.Append(0, Main.ClientMessage);
            }
            else
            {
                Environment.Append(Room.WiredManager.WiredOptions[id].length, Main.ClientMessage);
                for(int option : Room.WiredManager.WiredOptions[id])
                {
                    Environment.Append(option, Main.ClientMessage);
                }
            }

            Environment.Append(0, Main.ClientMessage); // selectiontype

            Environment.Append(Item.BaseItem.WiredType, Main.ClientMessage); // condition type
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }
}
