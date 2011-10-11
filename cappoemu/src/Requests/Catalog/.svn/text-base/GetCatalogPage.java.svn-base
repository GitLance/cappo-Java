package Requests.Catalog;

import Server.Catalog.CatalogItem;
import Server.Catalog.CatalogPage;
import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetCatalogPage extends Handler
{

    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int id = Main.DecodeInt();
        CatalogPage Page = Environment.Catalog.Pages.get(id);

        Environment.InitPacket(127, Main.ClientMessage);
        Environment.Append(id, Main.ClientMessage);
        if (Page.Layout.equals("frontpage3"))
        {
            Environment.Append("frontpage3", Main.ClientMessage);
            Environment.Append(3, Main.ClientMessage);
            Environment.Append("catalog_frontpage_headline2_de", Main.ClientMessage);
            Environment.Append("cine_2011_top_story_001", Main.ClientMessage);
            Environment.Append("", Main.ClientMessage);
            Environment.Append(11, Main.ClientMessage);
            Environment.Append("YOU\'RE IN THE MOVIES..", Main.ClientMessage);
            Environment.Append("...with the latest Habbowood items!", Main.ClientMessage);
            Environment.Append("Put on a show â€ºâ€º", Main.ClientMessage);
            Environment.Append("How to get Habbo Credits", Main.ClientMessage);
            Environment.Append("You can get Habbo Credits via Home Phone, Credit Card, Text Messaging, completing offers and more!  \r\nTo redeem your Habbo Prepaid Card enter your voucher code below.", Main.ClientMessage);
            Environment.Append("Redeem a voucher code here:", Main.ClientMessage);
            Environment.Append("Habbowood", Main.ClientMessage);
            Environment.Append("#FEFEFE", Main.ClientMessage);
            Environment.Append("#FEFEFE", Main.ClientMessage);
            Environment.Append("Want all the options?  Click here!", Main.ClientMessage);
            Environment.Append("magic.credits", Main.ClientMessage);
        }
        else if (Page.Layout.startsWith("default_3x3")) // default_3x3 and default_3x3_extrainfo
        {
            Environment.Append(Page.Layout, Main.ClientMessage);
            Environment.Append(3, Main.ClientMessage);
            Environment.Append(Page.Headline, Main.ClientMessage);
            Environment.Append(Page.Teaser, Main.ClientMessage);
            Environment.Append(Page.Special, Main.ClientMessage);
            Environment.Append(3, Main.ClientMessage);
            Environment.Append(Page.Text1, Main.ClientMessage);
            Environment.Append(Page.Text2, Main.ClientMessage);
            Environment.Append(Page.TextDetails, Main.ClientMessage);
        }
        else if (Page.Layout.equals("pets"))
        {
            Environment.Append("pets", Main.ClientMessage);
            Environment.Append(2, Main.ClientMessage);
            Environment.Append(Page.Headline, Main.ClientMessage);
            Environment.Append("", Main.ClientMessage);
            Environment.Append(4, Main.ClientMessage);
            Environment.Append(Page.Text1, Main.ClientMessage);
            Environment.Append(Page.Text2, Main.ClientMessage);
            Environment.Append(Page.TextDetails, Main.ClientMessage);
            Environment.Append(Page.TextTeaser, Main.ClientMessage);
        }
        else if (Page.Layout.equals("recycler") || Page.Layout.equals("cars") || Page.Layout.startsWith("pixel")) // pixeleffects - pixelrent
        {
            Environment.Append(Page.Layout, Main.ClientMessage);
            Environment.Append(2, Main.ClientMessage);
            Environment.Append(Page.Headline, Main.ClientMessage);
            Environment.Append(Page.Teaser, Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage);
            Environment.Append(Page.Text1, Main.ClientMessage);
        }
        else if (Page.Layout.equals("recycler_prizes") || Page.Layout.equals("club_gifts") || Page.Layout.equals("plasto") || Page.Layout.startsWith("spaces"))
        {
            Environment.Append(Page.Layout, Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage);
            Environment.Append(Page.Headline, Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage);
            Environment.Append(Page.Text1, Main.ClientMessage);
        }
        else if(Page.Layout.equals("recycler_info"))
        {
            Environment.Append("recycler_info", Main.ClientMessage);
            Environment.Append(2, Main.ClientMessage);
            Environment.Append("recycler_info", Main.ClientMessage);
            Environment.Append(Page.Teaser, Main.ClientMessage);
            Environment.Append(3, Main.ClientMessage);
            Environment.Append(Page.Text1, Main.ClientMessage);
            Environment.Append(Page.Text2, Main.ClientMessage);
            Environment.Append(Page.TextDetails, Main.ClientMessage);
        }
        else if (Page.Layout.equals("trophies"))
        {
            Environment.Append("trophies", Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage);
            Environment.Append(Page.Headline, Main.ClientMessage);
            Environment.Append(2, Main.ClientMessage);
            Environment.Append(Page.Text1, Main.ClientMessage);
            Environment.Append(Page.TextDetails, Main.ClientMessage);
        }
        else if (Page.Layout.equals("club_buy"))
        {
            Environment.Append("club_buy", Main.ClientMessage);
            Environment.Append(1, Main.ClientMessage);
            Environment.Append(Page.Headline, Main.ClientMessage);
            Environment.Append(0, Main.ClientMessage);
        }
        else if (Page.Layout.equals("pets2"))
        {
            Environment.Append("pets2", Main.ClientMessage);
            Environment.Append(2, Main.ClientMessage);
            Environment.Append(Page.Headline, Main.ClientMessage);
            Environment.Append(Page.Teaser, Main.ClientMessage);
            Environment.Append(4, Main.ClientMessage);
            Environment.Append(Page.Text1, Main.ClientMessage);
            Environment.Append(Page.Text2, Main.ClientMessage);
            Environment.Append(Page.TextDetails, Main.ClientMessage);
            Environment.Append(Page.TextTeaser, Main.ClientMessage);
        }
        else if (Page.Layout.equals("soundmachine") || Page.Layout.equals("mad_money"))
        {
            Environment.Append(Page.Layout, Main.ClientMessage);
            Environment.Append(2, Main.ClientMessage);
            Environment.Append(Page.Headline, Main.ClientMessage);
            Environment.Append(Page.Teaser, Main.ClientMessage);
            Environment.Append(2, Main.ClientMessage);
            Environment.Append(Page.Text1, Main.ClientMessage);
            Environment.Append(Page.Text2, Main.ClientMessage);
        }
        else
        {
            if (!Page.Layout.equals(""))
            {
                System.err.println("Not Found Page Layout: " + Page.Layout + " - " + Page.Id);
            }
            return;
        }
        
        Environment.Append(Page.Items.size(), Main.ClientMessage);
        for(CatalogItem Item : Page.Items.values())
        {
            Environment.Append(Item.Id, Main.ClientMessage);
            Environment.Append(Item.Name, Main.ClientMessage);
            Environment.Append(Item.Credits, Main.ClientMessage);
            Environment.Append(Item.Pixels, Main.ClientMessage);
            Environment.Append(Item.ExtraCost, Main.ClientMessage);
            Environment.Append(Item.ItemsSprite.size(), Main.ClientMessage);
            int pos = 0;
            for(String Type : Item.ItemsType)
            {
                int SpriteId = Item.ItemsSprite.get(pos);
                String ExtraData = Item.ItemsExtraData.get(pos);
                int Ammount = Item.ItemsAmmount.get(pos++);

                Environment.Append(Type, Main.ClientMessage);
                Environment.Append(SpriteId, Main.ClientMessage);
                Environment.Append(ExtraData, Main.ClientMessage);
                Environment.Append(Ammount, Main.ClientMessage);
                Environment.Append(-1, Main.ClientMessage);
            }
            Environment.Append(Item.MinRank, Main.ClientMessage);
        }
        Environment.Append(-1, Main.ClientMessage);
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
