package Server.ItemInteractor;

import Server.Connection;
import Server.Player;
import Server.Room.Room;
import Server.Room.RoomItem;
import Server.ServerMessage;

/*
 *****************
 * @author capos *
 *****************
*/

public class InteractorWiredFurnis extends Interactor
{
    @Override
    public void OnPlace(Connection User, RoomItem Item){ }

    @Override
    public void OnRemove(Connection User, RoomItem Item){ }

    @Override
    public void OnTrigger(Connection User, RoomItem Item, int Request, boolean UserHasRights)
    {
        if (!UserHasRights)
        {
            return;
        }

        Room Room = Item.Environment.RoomManager.GetRoom(Item.RoomId);
        
        if (Room == null)
        {
            return;
        }

        if(Item.ExtraData.equals("0"))
        {
            Item.ExtraData = "1";
        }
        else
        {
            Item.ExtraData = "0";
        }
        Item.UpdateNeeded = true;


        ServerMessage Message = new ServerMessage();
        User.Environment.InitPacket(653, Message);
        User.Environment.Append(Item.Id, Message);// Wired Item id
        User.Environment.EndPacket(User.Socket, Message);
    }
}