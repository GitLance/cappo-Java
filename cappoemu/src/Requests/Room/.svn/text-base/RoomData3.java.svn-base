package Requests.Room;

import Server.Connection;
import Server.Room.Room;
import Server.Room.RoomItem;
import Server.Room.RoomUser;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomData3 extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if (Main.Data.LoadingRoom == 0)
        {
            return;
        }

        Room Room = Environment.RoomManager.GetRoom(Main.Data.LoadingRoom);

        Main.Data.LoadingRoom = 0;

        if (Room == null)
        {
            return;
        }

        if (Room.UsersNow >= Room.UsersMax)
        {
            return;
        }

        int count = Room.UserList.length;

        Environment.InitPacket(28, Main.ClientMessage);
        Environment.Append(Room.UsersNow+Room.BotCounter+Room.PetCounter, Main.ClientMessage);
        for (int i = 0; i < count; i++)
        {
            RoomUser RoomUser = Room.UserList[i];

            if (RoomUser != null)
            {
                if (RoomUser.Type == 1)
                {
                    if (RoomUser.Client != null)
                    {
                        Environment.Append(RoomUser.Id, Main.ClientMessage);
                        Environment.Append(RoomUser.Client.Data.UserName, Main.ClientMessage);
                        Environment.Append(RoomUser.Client.Data.Motto, Main.ClientMessage);
                        Environment.Append(RoomUser.Client.Data.Look, Main.ClientMessage);
                        Environment.Append(RoomUser.VirtualId, Main.ClientMessage);
                        Environment.Append(RoomUser.X, Main.ClientMessage);
                        Environment.Append(RoomUser.Y, Main.ClientMessage);
                        Environment.Append(Double.toString(RoomUser.Z).replace(',', '.'), Main.ClientMessage);
                        Environment.Append(RoomUser.RotBody, Main.ClientMessage);
                        Environment.Append(RoomUser.Type, Main.ClientMessage);
                        Environment.Append(RoomUser.Client.Data.Sex==1 ? "M" : "F", Main.ClientMessage);
                        Environment.Append(-1, Main.ClientMessage);
                        Environment.Append(-1, Main.ClientMessage);
                        Environment.Append(-1, Main.ClientMessage);
                        Environment.Append("", Main.ClientMessage);
                        Environment.Append(RoomUser.Client.Data.AchievementsScore, Main.ClientMessage);
                    }
                }
                else
                {
                    Environment.Append(RoomUser.Id, Main.ClientMessage);
                    Environment.Append(RoomUser.BotData.Name, Main.ClientMessage);
                    Environment.Append(RoomUser.BotData.Motto, Main.ClientMessage);
                    Environment.Append(RoomUser.BotData.Look, Main.ClientMessage);
                    Environment.Append(RoomUser.VirtualId, Main.ClientMessage);
                    Environment.Append(RoomUser.X, Main.ClientMessage);
                    Environment.Append(RoomUser.Y, Main.ClientMessage);
                    Environment.Append(Double.toString(RoomUser.Z).replace(',', '.'), Main.ClientMessage);
                    Environment.Append(4, Main.ClientMessage);
                    Environment.Append(RoomUser.Type, Main.ClientMessage);
                    if (RoomUser.Type == 2) // Pet
                    {
                        Environment.Append(0, Main.ClientMessage);
                    }
                }
            }
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        // Model.StaticFurniMap se usaba cuando existian las salas publicas
        Environment.InitPacket(30, Main.ClientMessage);
        Environment.Append(0, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        Environment.InitPacket(32, Main.ClientMessage);
        Environment.Append(Room.FloorItems.size(), Main.ClientMessage);
        for(RoomItem Item : Room.FloorItems)
        {
            Environment.Append(Item.Id, Main.ClientMessage);
            Environment.Append(Item.BaseItem.SpriteId, Main.ClientMessage);
            Environment.Append(Item.X, Main.ClientMessage);
            Environment.Append(Item.Y, Main.ClientMessage);
            Environment.Append(Item.Rot, Main.ClientMessage);
            Environment.Append(Double.toString(Item.Z).replace(",", "."), Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
            Environment.Append(Item.ExtraData, Main.ClientMessage);
            Environment.Append(-1, Main.ClientMessage);
            Environment.Append(true, Main.ClientMessage); // knownAsUsable
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        Environment.InitPacket(45, Main.ClientMessage);
        Environment.Append(Room.WallItems.size(), Main.ClientMessage);
        for(RoomItem Item : Room.WallItems)
        {
            Environment.Append(Integer.toString(Item.Id), Main.ClientMessage);
            Environment.Append(Item.BaseItem.SpriteId, Main.ClientMessage);
            Environment.Append(Item.WallPos, Main.ClientMessage);
            if(Item.BaseItem.Interaction == Environment.Furniture.POSTIT)
            {
                Environment.Append(Item.ExtraData.split(" ")[0], Main.ClientMessage);
            }
            else
            {
                Environment.Append(Item.ExtraData, Main.ClientMessage);
            }
            Environment.Append(true, Main.ClientMessage); // knownAsUsable
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        Room.AddUserToRoom(Main);

        Environment.InitPacket(472, Main.ClientMessage);
        Environment.Append((Room.Flags & Server.rmHideWall) == Server.rmHideWall, Main.ClientMessage);
        Environment.Append(Room.WallAnchor, Main.ClientMessage);
        Environment.Append(Room.FloorAnchor, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        Environment.InitPacket(471, Main.ClientMessage);
        Environment.Append(true, Main.ClientMessage);// is private?
        Environment.Append(Room.Id, Main.ClientMessage);
        Environment.Append(Room.CheckRights(Main.Data, true), Main.ClientMessage); // Is Owner
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        Environment.InitPacket(822, Main.ClientMessage);
        Environment.Append(false, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        Environment.InitPacket(823, Main.ClientMessage);
        Environment.Append(false, Main.ClientMessage);
        Environment.Append("", Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        Room.SerializeStatusUpdates(Main);

        count = Room.UserList.length;

        for (int i = 0; i < count; i++)
        {
            if (Room.UserList[i] != null)
            {
                if (Room.UserList[i].IsDancing)
                {
                    Environment.InitPacket(480, Main.ClientMessage);
                    Environment.Append(Room.UserList[i].VirtualId, Main.ClientMessage);
                    Environment.Append(Room.UserList[i].DanceId, Main.ClientMessage);
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                }

                if (Room.UserList[i].IsAsleep)
                {
                    Environment.InitPacket(486, Main.ClientMessage);
                    Environment.Append(Room.UserList[i].VirtualId, Main.ClientMessage);
                    Environment.Append(true, Main.ClientMessage);
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                }

                if (Room.UserList[i].Type == 1)
                {
                    if (Room.UserList[i].CurrentEffect > 0)
                    {
                        Environment.InitPacket(485, Main.ClientMessage);
                        Environment.Append(Room.UserList[i].VirtualId, Main.ClientMessage);
                        Environment.Append(Room.UserList[i].CurrentEffect, Main.ClientMessage);
                        Environment.EndPacket(Main.Socket, Main.ClientMessage);
                    }
                }
            }
        }

/*
        Environment.InitPacket(316,true);
        Environment.AppendInt(1773);
        Environment.AppendStringWithBreak("Queremos tu opinion!");
        Environment.EndPacket();
        Main.SendData();*/

        Room.WiredManager.ParseWiredEntersRoom(Main);
    }
}
