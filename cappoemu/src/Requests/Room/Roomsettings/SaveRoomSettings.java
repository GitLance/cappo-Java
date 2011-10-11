package Requests.Room.Roomsettings;

import Server.Connection;
import Server.Room.Room;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class SaveRoomSettings extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int RoomId = Main.DecodeInt();

        if (RoomId > 0)
        {
            Room Room = Environment.RoomManager.GetRoom(RoomId);

            if (Room != null)
            {
                if (!Room.CheckRights(Main.Data, true))
                {
                    return;
                }

                String Name = Main.DecodeString();
                if (Name.length() < 4)
                {
                    return;
                }
                Room.Name = Name;
                Room.Description = Main.DecodeString();
                int State = Main.DecodeInt();
                if (State < 0 || State > 2)
                {
                    return;
                }
                Room.State = State;
                Room.Password = Main.DecodeString();
                int MaxUsers = Main.DecodeInt();
                if(MaxUsers % 5 != 0)
                {
                    return;
                }
                if (MaxUsers < 10 || MaxUsers > 25)
                {
                    return;
                }
                Room.UsersMax = MaxUsers;
                int CategoryId = Main.DecodeInt();
                /*FlatCat FlatCat = UberEnvironment.GetGame().GetNavigator().GetFlatCat(CategoryId);

                if (FlatCat == null)
                {
                    return;
                }

                if(FlatCat.MinRank == 2)
                {
                    if(!IsAdmin())
                    {
                        SendNotif("You are not allowed to use this category. Your room has been moved to no category instead.", true);
                        SendData();
                        CategoryId = 0;
                    }
                }
                else if(FlatCat.MinRank == 3)
                {
                    if(!IsOwner())
                    {
                        SendNotif("You are not allowed to use this category. Your room has been moved to no category instead.", true);
                        SendData();
                        CategoryId = 0;
                    }
                }*/
                Room.Category = CategoryId;
                int TagCount = Main.DecodeInt();
                if (TagCount > 2)
                {
                    return;
                }
                for(String Tag : Room.Tags)
                {
                    Environment.RoomManager.RemoveTag(Tag);
                }
                Room.Tags.clear();
                for (int i = 0; i < TagCount; i++)
                {
                    String Tag =  Main.DecodeString().toLowerCase();
                    Room.Tags.add(Tag);
                    Environment.RoomManager.AddTag(Tag);
                }
                
                Room.SetFlag(Server.rmAllowPets,Main.DecodeCheckBox());
                Room.SetFlag(Server.rmAllowPetsEat,Main.DecodeCheckBox());
                Room.SetFlag(Server.rmAllowWalkthrough,Main.DecodeCheckBox());
                Room.SetFlag(Server.rmHideWall,Main.DecodeCheckBox());

                Room.WallAnchor = Main.DecodeInt();
                Room.FloorAnchor = Main.DecodeInt();

                // Room Settings Saved
                Environment.InitPacket(467, Main.ClientMessage);
                Environment.Append(Room.Id, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);

                // Room Updated
                Environment.InitPacket(456, Main.ClientMessage);
                Environment.Append(Room.Id, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);

                Environment.InitPacket(472, Main.ClientMessage);
                Environment.Append((Room.Flags & Server.rmHideWall) == Server.rmHideWall, Main.ClientMessage);
                Environment.Append(Room.WallAnchor, Main.ClientMessage);
                Environment.Append(Room.FloorAnchor, Main.ClientMessage);
                Environment.EndPacket(Main.Socket, Main.ClientMessage);
            }
        }
    }
}
