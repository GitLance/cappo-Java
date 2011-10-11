package Server.ItemInteractor;

import Server.Connection;
import Server.Player;
import Server.Room.Room;
import Server.Room.RoomItem;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class InteractorBBTimer extends Interactor
{
    @Override
    public void OnPlace(Connection User, RoomItem Item)
    {
        Room Room = Item.Environment.RoomManager.GetRoom(Item.RoomId);

        if (Room == null)
        {
            return;
        }

        Room.BB_Points_R = 0;
        Room.BB_Points_G = 0;
        Room.BB_Points_B = 0;
        Room.BB_Points_Y = 0;
        Item.Timer = 60;
    }

    @Override
    public void OnRemove(Connection User, RoomItem Item)
    {

    }

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

        if (Request == 1) // Iniciar Pausar
        {
            if ((Room.Flags & Server.rmBBanzai) == Server.rmBBanzai)
            {
                Room.SetFlag(Server.rmBBanzai,false);
                Item.ActiveTask = 0;
            }
            else
            {
                Room.ONSTARBB();
                
                Room.SetFlag(Server.rmBBanzai,true);
                Item.TaskSteps = 0;
                Item.ActiveTask = 20;
                Room.WiredManager.ParseWiredGameStart();
            }
        }
        else // Reset y Agregar Tiempo
        {
            if ((Room.Flags & Server.rmBBanzai) == Server.rmBBanzai)
            {
                Room.SetFlag(Server.rmBBanzai,false);
                Item.ActiveTask = 0;
            }

            Room.BB_Points_R = 0;
            Room.BB_Points_G = 0;
            Room.BB_Points_B = 0;
            Room.BB_Points_Y = 0;

            if (Item.Timer >= 600 || Item.Timer < 1) // 10 min maximo
            {
                Item.Timer = 60; // 1 minuto
            }
            else
            {
                Item.Timer -= (Item.Timer % 60); // Multiplo
                Item.Timer += 60; // + 1 minuto
            }
            
            Item.ExtraData = Integer.toString(Item.Timer);
            Item.UpdateNeeded = true;

            for(RoomItem IItem : Room.FloorItems)
            {
                if (IItem.BaseItem.Interaction >= Room.Environment.Furniture.BATTLEBANZAI_SCORE_R && IItem.BaseItem.Interaction <=  Room.Environment.Furniture.BATTLEBANZAI_PATCH)
                {
                    IItem.BattleSteep = 0;
                    if(!IItem.ExtraData.equals("0"))
                    {
                        IItem.ExtraData = "0";
                        IItem.UpdateNeeded = true;
                    }
                }
            }
        }
    }
}
