package Server.ItemInteractor;

import Server.Connection;
import Server.Room.Room;
import Server.Room.RoomItem;

/*
 *****************
 * @author capos *
 *****************
*/

public class InteractorTeleport extends Interactor
{
    @Override
    public void OnPlace(Connection User, RoomItem Item)
    {
        Item.ExtraData = "0";
    }

    @Override
    public void OnRemove(Connection User, RoomItem Item)
    {
        Item.ExtraData = "0";
    }

    @Override
    public void OnTrigger(Connection User, RoomItem Item, int Request, boolean UserHasRights)
    {
        if(User.Data.TeleporterId != 0) return; // esta usando un teleport

        Room Room = Item.Environment.RoomManager.GetRoom(Item.RoomId);

        if (Room != null)
        {
            int[] xy = Item.SquareInFront();

            if ((User.Data.RoomUser.X == xy[0] && User.Data.RoomUser.Y == xy[1]) || (User.Data.RoomUser.X == Item.X && User.Data.RoomUser.Y == Item.Y))
            {
                if(User.Data.RoomUser.ActiveTask == 0)
                {
                    int TeleId = Item.Environment.Teleports.GetTele(Item.Id);

                    if(TeleId != -1)
                    {
                        Item.ExtraData = "2";
                        Item.UpdateNeeded = true;

                        Item.TaskSteps = 2;
                        Item.ActiveTask = 1;

                        User.Data.RoomUser.AllowOverride = true;
                        User.Data.RoomUser.MoveTo(Item.X, Item.Y, false);
                        User.Data.RoomUser.TaskSteps = 2;
                        User.Data.RoomUser.ActiveTask = 1;

                        User.Data.TeleporterId = TeleId;
                    }
                }
            }
            else
            {
                User.Data.RoomUser.MoveTo(xy[0], xy[1], true);
            }
        }
    }
}