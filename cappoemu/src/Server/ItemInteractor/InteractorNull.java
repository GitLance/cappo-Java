package Server.ItemInteractor;

import Server.Connection;
import Server.Room.RoomItem;

/*
 *****************
 * @author capos *
 *****************
*/

public class InteractorNull extends Interactor
{
    @Override
    public void OnPlace(Connection User, RoomItem Item)
    {

    }

    @Override
    public void OnRemove(Connection User, RoomItem Item)
    {

    }

    @Override
    public void OnTrigger(Connection User, RoomItem Item, int Request, boolean UserHasRights)
    {

    }
}