package Requests.Inventory.Trading;

import Server.Connection;
import Server.Room.Room;
import Server.Trade;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class OpenTrading extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if(Main.Data.Trade != null)
        {
            return;
        }

        Room Room = Environment.RoomManager.GetRoom(Main.Data.CurrentRoom);

        if (Room == null)
        {
            return;
        }

        int User = Room.GetRoomUserByVirtualId(Main.DecodeInt());

        if (User != -1)
        {
            if (Room.UserList[User].Client.Data.Trade != null)
            {
                return;
            }
         
            Main.Data.RoomUser.SetStatus("trd", "");
            Room.UserList[User].SetStatus("trd", "");

            Trade trade = new Trade(Environment, Main.Data, Room.UserList[User].Client.Data);
            trade.SetTrade();
        }
    }
}
