package Requests.Room;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomData1 extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        if (Main.Data.LoadingRoom != 0)
        {
            Environment.InitPacket(297, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
            
            /*
                QC
                footylamp_campaign{2}
                footylamp_campaign_ing{2}
                ads_711{2}
                ads_711c{2}
                ads_711shelf{2}
                ads_711shelfcmp{2}
                ads_oc_soda{2}
                ads_oc_soda_cmp{2}
                ads_boost_surfb{2}
                ads_boost_surfbcmp{2}
                ads_target_wall{2}
                ads_target_wall_cmp{2}
                ads_capri_chair{2}
                ads_capri_chair_camp{2}
                ads_capri_lava{2}
                ads_capri_lava_camp{2}
                ads_capri_arcade{2}
                ads_capri_arcade_camp{2}
                ads_pepsi0{2}
                ads_pepsi0_camp{2}
                ads_oc_soda_cherry{2}
                ads_oc_soda_cherry_cmp{2}
                ads_rangocactus{2}
                ads_rangocactus_camp{2}
                ads_wowpball{2}
                ads_wowpball_camp{2}
                {1}
             */
        }
    }
}
