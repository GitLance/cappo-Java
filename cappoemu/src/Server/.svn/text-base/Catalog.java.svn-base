package Server;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/*
 *****************
 * @author capos *
 *****************
*/

public class Catalog
{
    public Map<Integer, CatalogPage> Pages = new HashMap<Integer, CatalogPage>();
    
    public Map<Integer, List<CatalogPage>> CatalogMap = new HashMap<Integer, List<CatalogPage>>();
    
    public Catalog(Server Server) throws Exception
    {
        DatabaseClient DB = new DatabaseClient(Server.DataBase);
        ResultSet table = DB.Query("SELECT * FROM catalog_pages ORDER BY id ASC;");

        while (table.next())
        {
            CatalogPage Page = new CatalogPage();
            Page.Id = table.getInt("id");
            Page.ParentId = table.getInt("parent_id");
            Page.Caption = table.getString("caption");
            Page.Visible = (table.getInt("visible") == 1);
            Page.Enabled = (table.getInt("enabled") == 1);
            Page.StaffOnly = (table.getInt("staff_only") == 1);
            Page.IconColor = table.getInt("icon_color");
            Page.IconImage = table.getInt("icon_image");
            Page.Layout = table.getString("page_layout");
            Page.Headline = table.getString("page_headline");
            Page.Teaser = table.getString("page_teaser");
            Page.Special = table.getString("page_special");
            Page.Text1 = table.getString("page_text1");
            Page.Text2 = table.getString("page_text2");
            Page.TextDetails = table.getString("page_text_details");
            Page.TextTeaser = table.getString("page_text_teaser");
            Pages.put(Page.Id, Page);
            
            if(CatalogMap.containsKey(Page.ParentId))
            {
                List<CatalogPage> a = CatalogMap.get(Page.ParentId);
                a.add(Page);
            }
            else
            {
                List<CatalogPage> a = new ArrayList<CatalogPage>(50);
                a.add(Page);
                CatalogMap.put(Page.ParentId, a);
            }
        }
        
        table.close();
        
        ResultSet item;

        for(CatalogPage Page : Pages.values())
        {
            item = DB.Query("SELECT * FROM catalog_items WHERE page_id = '" + Page.Id + "';");

            while (item.next())
            {
                CatalogItem Item = new CatalogItem();
                Item.Id = item.getInt("id");
                Item.Name = item.getString("name");
                Item.Credits = item.getInt("cost_credits");
                Item.Pixels = item.getInt("cost_pixels");
                Item.ExtraCost = item.getInt("cost_extra");
                Item.MinRank = item.getInt("min_rank");
                Page.Items.put(Item.Id,Item);
            }
            
            item.close();
            
            for(int Key : Page.Items.keySet())
            {
                CatalogItem Item = Page.Items.get(Key);

                item = DB.Query("SELECT * FROM sub_items WHERE id = '" + Key + "';");

                int pos = 0;
                while (item.next())
                {
                    Item.ItemsType.add(pos, item.getString("item_type"));
                    Item.ItemsSprite.add(pos, item.getInt("item_sprite"));
                    Item.ItemsExtraData.add(pos, item.getString("extra_data"));
                    Item.ItemsAmmount.add(pos++, item.getInt("item_amount"));
                }

                item.close();
            }
        }

        DB.Close();
    }

    public static class CatalogItem
    {
        public int Id;
        public String Name;
        public int Credits;
        public int Pixels;
        public int ExtraCost;
        public int MinRank;

        public List<String> ItemsType = new ArrayList<String>();
        public List<Integer> ItemsSprite = new ArrayList<Integer>();
        public List<String> ItemsExtraData = new ArrayList<String>();
        public List<Integer> ItemsAmmount = new ArrayList<Integer>();
    }

    public static class CatalogPage
    {
        public int Id;
        public int ParentId;
        public String Caption;
        public boolean Visible;
        public boolean Enabled;
        public boolean StaffOnly;
        public int IconColor;
        public int IconImage;
        public String Layout;
        public String Headline;
        public String Teaser;
        public String Special;
        public String Text1;
        public String Text2;
        public String TextDetails;
        public String TextTeaser;

        public Map<Integer, CatalogItem> Items = new HashMap<Integer, CatalogItem>();
    }
}
