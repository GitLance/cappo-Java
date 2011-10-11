package Requests.Inventory.Achievements;

import Server.Connection;

import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetAchievements extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(436, Main.ClientMessage);

        Environment.Append(1, Main.ClientMessage); // count archivements

        Environment.Append(1, Main.ClientMessage); // Id
        Environment.Append(4, Main.ClientMessage); // Next Nivel
        Environment.Append("ACH_TagC6", Main.ClientMessage); // badge
        Environment.Append(10, Main.ClientMessage); // Max Nivel
        Environment.Append(10, Main.ClientMessage); // Next Nevel Reward (Pixels)
        /*  Reward Image Type
            0 http://images.habbo.com/c_images/Quests/quest_pixel.png
            1 http://images.habbo.com/c_images/Quests/icon_xmas10_pixeltype.png
            2 http://images.habbo.com/c_images/Quests/icon_val11_pixeltype.png
            4 http://images.habbo.com/c_images/Quests/quest_shells.png
        */
        Environment.Append(0, Main.ClientMessage); // Reward Image Type
        Environment.Append(3, Main.ClientMessage); // Have
        Environment.Append(true, Main.ClientMessage); // Only 1 level
        /*  Categories
            pets
            explore
            room_builder
            social
            identity
            music
            CAT NOT AVAILABLE
            games
         */
        Environment.Append("identity", Main.ClientMessage); // category
        Environment.Append(10, Main.ClientMessage); // total levels

        
        Environment.Append("identity", Main.ClientMessage); // Default AchievementCategory??

        Environment.EndPacket(Main.Socket, Main.ClientMessage);
/*

        // Unlock
        InitData(438,true);
        AppendInt(110);
        AppendInt(30);
        AppendInt(0);
        EndPacket();

        // Unlock
        InitData(437,false);
        AppendInt(19); // ??
        AppendInt(1); // Id
        AppendInt(5); // Next level
        AppendStringWithBreak("ACH_EmailVerification"); // badge
        AppendInt(10); // Need
         AppendInt(100); // + Archievement Score
        AppendInt(200); // + Pixels
        AppendStringWithBreak("");
        EndPacket();

        SendData();*/

        return;
    }
}