package Server;


import Server.ItemInteractor.Interactor;
import Server.ItemInteractor.InteractorTrax;
import Server.ItemInteractor.InteractorWiredFurnis;
import Server.ItemInteractor.InteractorBBTimer;
import Server.ItemInteractor.InteractorNull;
import Server.ItemInteractor.InteractorTeleport;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;

/*
 *****************
 * @author capos *
 *****************
*/

public class Furniture
{
    public static final int BATTLEBANZAI_PIRAMID    = 1;
    public static final int BATTLEBANZAI_COUNTER    = 2;
    public static final int BATTLEBANZAI_SCORE_R    = 3;
    public static final int BATTLEBANZAI_SCORE_G    = 4;
    public static final int BATTLEBANZAI_SCORE_B    = 5;
    public static final int BATTLEBANZAI_SCORE_Y    = 6;
    public static final int BATTLEBANZAI_PATCH      = 7;
    public static final int BATTLEBANZAI_GATER      = 8;
    public static final int BATTLEBANZAI_GATEB      = 9;
    public static final int BATTLEBANZAI_GATEG      = 10;
    public static final int BATTLEBANZAI_GATEY      = 11;
    public static final int WIRED_CONDITION         = 12;
    public static final int WIRED_TRIGGER           = 13;
    public static final int WIRED_EFFECT            = 14;
    public static final int WIRED_OTHER             = 15;
    public static final int XMAS2010                = 16;
    public static final int TELEPORT                = 17;
    public static final int WALLPAPER               = 18;
    public static final int LANDSCAPE               = 19;
    public static final int ROLLER                  = 20;
    public static final int SKATES                  = 21;
    public static final int POSTIT                  = 22;
    public static final int DIMMER                  = 23;
    public static final int TROPHY                  = 24;
    public static final int WATER1                  = 25;
    public static final int WATER2                  = 26;
    public static final int WATER3                  = 27;
    public static final int FLOOR                   = 28;
    public static final int BED                     = 29;
    public static final int BALL                    = 30;


    public Map<Integer, Item> Items = new HashMap<Integer, Item>();
    public Map<Integer, Item> IdsBySprite = new HashMap<Integer, Item>();

    private Interactor TeleInteractor = new InteractorTeleport();
    private Interactor InteractorTrax = new InteractorTrax();
    private Interactor BBTimerInteractor = new InteractorBBTimer();
    private Interactor NullInteractor = new InteractorNull();

    private Interactor WiredInteractor = new InteractorWiredFurnis();

    public Furniture(String[] DATA) throws Exception
    {
        DatabaseClient DB = new DatabaseClient(DATA);
        ResultSet table = DB.Query("SELECT * FROM furniture;");

        while (table.next())
        {
            Item Item = new Item();

            Item.Id = table.getInt("id");
            Item.PublicName = table.getString("public_name");
            Item.ItemName = table.getString("item_name");
            Item.Type = table.getString("type");
            Item.Width = table.getInt("width");
            Item.Length = table.getInt("length");
            Item.SpriteId = table.getInt("sprite_id");
            Item.VendingIds = table.getString("vending_ids");

            // Pending
            Item.InteractionCount = 0;

            if(Item.Type.equals("s"))
            {
                String ItemName = Item.ItemName.toLowerCase();

                if(Item.PublicName.startsWith("WIRED Trigger:"))
                {
                    if(Item.PublicName.contains("User Says Keyword"))
                    {
                        Item.WiredType = 0;
                    }
                    if(Item.PublicName.contains("User Walks On Furni"))
                    {
                        Item.WiredType = 1;
                    }
                    else if(Item.PublicName.contains("Repeat Effect"))
                    {
                        Item.WiredType = 3;
                    }
                    else if(Item.PublicName.contains("At Set Time"))
                    {
                        Item.WiredType = 6;
                    }
                    else if(Item.PublicName.contains("User Enters Room"))
                    {
                        Item.WiredType = 7;
                    }
                    else if(Item.PublicName.contains("Game Starts"))
                    {
                        Item.WiredType = 8;
                    }
                    else if(Item.PublicName.contains("Score Is Achieved"))
                    {
                        Item.WiredType = 10;
                    }
                    
                    Item.Interactor = WiredInteractor;
                    Item.Interaction = WIRED_TRIGGER;
                    Item.Walkable = true;
                    Item.Stackable = true;
                    Item.Height = 0.5;
                }
                else if(Item.PublicName.startsWith("WIRED Effect:"))
                {
                    if(Item.PublicName.contains("Show Message"))
                    {
                        Item.WiredType = 7;
                    }
                    else if(Item.PublicName.contains("Move And Rotate Furni"))
                    {
                        Item.WiredType = 13;
                    }
                    else if(Item.PublicName.contains("Give Points"))
                    {
                        Item.WiredType = 14;
                    }

                    Item.Interactor = WiredInteractor;
                    Item.Interaction = WIRED_EFFECT;
                    Item.Walkable = true;
                    Item.Stackable = true;
                    Item.Height = 0.5;
                }
                else if(Item.PublicName.startsWith("WIRED Condition:"))
                {
                    if(Item.PublicName.contains("More Than x Secs Elapsed Since Timer Reset"))
                    {
                        Item.WiredType = 3;
                    }
                    else if(Item.PublicName.contains("Less Than x Secs Elapsed Since Timer Reset"))
                    {
                        Item.WiredType = 4;
                    }
                    else if(Item.PublicName.contains("Furni has users"))
                    {
                        Item.WiredType = 5;
                    }
                    else if(Item.PublicName.contains("Has Furni On"))
                    {
                        Item.WiredType = 7;
                    }
                    else if(Item.PublicName.contains("Furni States And Positions Match"))
                    {
                        Item.WiredType = 8;
                    }

                    Item.Interactor = WiredInteractor;
                    Item.Interaction = WIRED_CONDITION;
                    Item.Walkable = true;
                    Item.Stackable = true;
                    Item.Height = 0.5;
                }
                else if(ItemName.substring(0, 3).equals("bw_"))
                {
                    if(ItemName.equals("bw_water_1"))
                    {
                        Item.Interaction = WATER1;
                        Item.Walkable = true;
                    }
                    else if(ItemName.equals("bw_water_2"))
                    {
                        Item.Interaction = WATER2;
                        Item.Walkable = true;
                    }
                    else if(ItemName.equals("hween10_pond"))
                    {
                        Item.Interaction = WATER3;
                        Item.Walkable = true;
                    }
                    else if(ItemName.equals("bw_tele"))
                    {
                        Item.Interaction = TELEPORT;
                        Item.Interactor = TeleInteractor;
                    }
                }
                else if(ItemName.substring(0, 3).equals("bb_"))
                {
                    if(ItemName.equals("bb_gate_r"))
                    {
                        Item.Interaction = BATTLEBANZAI_GATER;
                        Item.Walkable = true;
                    }
                    else if(ItemName.equals("bb_gate_g"))
                    {
                        Item.Interaction = BATTLEBANZAI_GATEG;
                        Item.Walkable = true;
                    }
                    else if(ItemName.equals("bb_gate_b"))
                    {
                        Item.Interaction = BATTLEBANZAI_GATEB;
                        Item.Walkable = true;
                    }
                    else if(ItemName.equals("bb_gate_y"))
                    {
                        Item.Interaction = BATTLEBANZAI_GATEY;
                        Item.Walkable = true;
                    }
                    else if(ItemName.equals("bb_patch1"))
                    {
                        Item.Interaction = BATTLEBANZAI_PATCH;
                        Item.Walkable = true;
                    }
                    else if(ItemName.equals("bb_counter"))
                    {
                        Item.Interaction = BATTLEBANZAI_COUNTER;
                        Item.Interactor = BBTimerInteractor;
                    }
                    else if(ItemName.equals("bb_score_r"))
                    {
                        Item.Interaction = BATTLEBANZAI_SCORE_R;
                    }
                    else if(ItemName.equals("bb_score_g"))
                    {
                        Item.Interaction = BATTLEBANZAI_SCORE_G;
                    }
                    else if(ItemName.equals("bb_score_b"))
                    {
                        Item.Interaction = BATTLEBANZAI_SCORE_B;
                    }
                    else if(ItemName.equals("bb_score_y"))
                    {
                        Item.Interaction = BATTLEBANZAI_SCORE_Y;
                    }
                    else if(ItemName.equals("bb_pyramid"))
                    {
                        Item.Interaction = BATTLEBANZAI_PIRAMID;
                    }
                }
                else if(ItemName.contains("deal_com"))
                {
                    if(ItemName.contains("deal_com_shallow"))
                    {
                        Item.Interaction = WATER1;
                        Item.Walkable = true;
                    }
                    else if(ItemName.contains("deal_com_deep"))
                    {
                        Item.Interaction = WATER2;
                        Item.Walkable = true;
                    }
                    else if(ItemName.contains("bbtiles"))
                    {
                        Item.Interaction = BATTLEBANZAI_PATCH;
                        Item.Walkable = true;
                    }
                    else if(ItemName.contains("pyramids"))
                    {
                        Item.Interaction = BATTLEBANZAI_PIRAMID;
                    }
                    else if(ItemName.contains("bbtele"))
                    {
                        // pending..
                    }
                }
                else if(Item.PublicName.toLowerCase().contains("chair"))
                {
                    Item.IsSeat = true;
                }
                else if(ItemName.contains("chair"))
                {
                    Item.IsSeat = true;
                }
                else if(Item.PublicName.toLowerCase().contains("bed"))
                {
                    Item.Interaction = BED;
                    Item.IsSeat = true;
                }
                else if(Item.PublicName.toLowerCase().contains("rug"))
                {
                    Item.Walkable = true;
                }
                else if(Item.PublicName.toLowerCase().contains("corner"))
                {
                    Item.Stackable = true;
                    Item.Height = 1.7;
                }
                else if(ItemName.contains("deal_roller"))
                {
                    Item.Interaction = SKATES;
                    Item.Walkable = true;
                }
                else if(ItemName.equals("val11_floor"))
                {
                    Item.Interaction = SKATES;
                    Item.Walkable = true;
                }
                else if(ItemName.contains("fball_ball") || ItemName.contains("bb_puck"))
                {
                    Item.Height = 0.0;
                    Item.Interaction = BALL;
                    Item.Walkable = true;
                }
                else if(ItemName.contains("seat"))
                {
                    Item.IsSeat = true;
                }
                else if(ItemName.contains("sofa"))
                {
                    Item.IsSeat = true;
                }
                else if(ItemName.contains("STP"))
                {
                    Item.IsSeat = true;
                }
                else if(ItemName.contains("beanbag"))
                {
                    Item.IsSeat = true;
                }
                else if(ItemName.contains("BTP"))
                {
                    Item.Stackable = true;
                    Item.Height = 1.2;
                }
                else if(Item.PublicName.equals("Doormat"))
                {
                    Item.Walkable = true;
                }
                else if(ItemName.startsWith("a2 ovi"))
                {
                    Item.Interaction = TELEPORT;
                    Item.Interactor = TeleInteractor;
                }
                else if(ItemName.startsWith("jukebox"))
                {
                    Item.Interactor = InteractorTrax;
                }
                else if(ItemName.contains("tele"))
                {
                    Item.Interaction = TELEPORT;
                    Item.Interactor = TeleInteractor;
                }
                else if(ItemName.contains("elevator"))
                {
                    Item.Interaction = TELEPORT;
                    Item.Interactor = TeleInteractor;
                }
                else if(ItemName.contains("prizetrophy"))
                {
                    Item.Interaction = TROPHY;
                }
            }
            else if(Item.Type.equals("i"))
            {
                if(Item.ItemName.contains("wallpaper_"))
                {
                    Item.Interaction = WALLPAPER;
                }
                else if (Item.ItemName.contains("landscape_"))
                {
                    Item.Interaction = LANDSCAPE;
                }
                else if (Item.ItemName.contains("floor_single_"))
                {
                    Item.Interaction = FLOOR;
                }
                else if(Item.ItemName.contains("dimmer"))
                {
                    Item.Interaction = DIMMER;
                }
                else if(Item.ItemName.contains("stick"))
                {
                    Item.Interaction = POSTIT;
                }
            }

            if(Item.Interactor == null)
            {
                Item.Interactor = NullInteractor;
            }

            Items.put(Item.Id, Item);
            IdsBySprite.put(Item.SpriteId, Item);
        }

        table.close();
        DB.Close();
    }

    public class Item
    {
        public int Id;
        public String PublicName;
        public String ItemName;
        public String Type;
        public int Interaction;
        public int SpriteId;
        public int Width;
        public int Length;
        public int InteractionCount;
        public int WiredType;
        public String VendingIds;
        public double Height = 0.1;
        public boolean Stackable = false;
        public boolean Walkable = false;
        public boolean IsSeat = false;
        public boolean AllowRecycle = true;
        public boolean AllowTrade = true;
        public boolean AllowMarketplaceSell = true;
        public boolean AllowGift = true;
        public boolean AllowInventoryStack = true;

        public Interactor Interactor;
    }
}