package Server;

import Requests.Handler;
import Requests.Navigator.AddFavouriteRoom;
import Requests.Catalog.PurchaseFromCatalog;
import Requests.Room.RoomCanCreate;
import Requests.Users.ApproveName;
import Requests.Handshake.VersionCheck;
import Requests.Handshake.GenerateSecretKey;
import Requests.Handshake.Disconnect;
import Requests.Friendlist.FindNewFriends;
import Requests.Friendlist.AcceptBuddy;
import Requests.Friendlist.FollowFriend;
import Requests.Friendlist.FriendListUpdate;
import Requests.Friendlist.RequestBuddy;
import Requests.Inventory.Purse.CreditsBalance;
import Requests.Marketplace.GetMarketplaceConfiguration;
import Requests.Catalog.GetGiftWrappingConfiguration;
import Requests.Catalog.GetClubOffers;
import Requests.Catalog.GetClubGiftInfo;
import Requests.Handshake.InitCrypto;
import Requests.Navigator.GetUserFlatCats;
import Requests.Friendlist.GetBuddyRequests;
import Requests.Room.RoomPopularTags;
import Requests.Recycler.GetRecyclerStatus;
import Requests.Recycler.GetRecyclerPrizes;
import Requests.ExtendSubscriptionPromo;
import Requests.Inventory.Badges.GetBadgePointLimits;
import Requests.Users.ScrGetUserInfo;
import Requests.Sound.GetSongInfo;
import Requests.Sound.GetSoundSettings;
import Requests.Friendlist.MessengerInit;
import Requests.Handshake.UniqueID;
import Requests.Marketplace.GetMarketplaceCanMakeOffer;
import Requests.Room.Session.OpenFlatConnection;
import Requests.Users.GetIgnoredUsers;
import Requests.Catalog.GetSellablePetBreeds;
import Requests.Room.Engine.GetPetCommands;
import Requests.Tracking.LatencyPingRequest;
import Requests.Tracking.LatencyPingReport;
import Requests.Handshake.Pong;
import Requests.Catalog.GetIsOfferGiftable;
import Requests.Navigator.DeleteFavouriteRoom;
import Requests.Room.LookUpdate;
import Requests.Room.Avatar.Dance;
import Requests.Inventory.Avatareffect.AvatarEffectActivated;
import Requests.Inventory.Achievements.GetAchievements;
import Requests.Room.GroupBadgesGet;
import Requests.Inventory.Furni.RequestFurniInventory;
import Requests.Room.RoomData1;
import Requests.Room.Engine.GetRoomEntryData;
import Requests.Room.RoomData3;
import Requests.Room.Roomsettings.GetRoomSettings;
import Requests.Room.RoomInfo;
import Requests.Users.GetSelectedBadges;
import Requests.Handshake.InfoRetrieve;
import Requests.Room.UserTagsGet;
import Requests.Room.WardrobeGet;
import Requests.Room.GoToHotelView;
import Requests.Room.LookAt;
import Requests.Room.Move;
import Requests.Room.MoveFloorItem;
import Requests.Room.ItemPlace;
import Requests.Room.RateRoomGive;
import Requests.Advertisement.GetInterstitial;
import Requests.Sound.AddJukeboxDisk;
import Requests.Inventory.Badges.GetBadges;
import Requests.Inventory.Badges.SetActivatedBadges;
import Requests.Room.Roomsettings.SaveRoomSettings;
import Requests.Room.WardrobeSave;
import Requests.Room.MottoUpdate;
import Requests.Chat.TypingStart;
import Requests.Chat.TypingStop;
import Requests.Room.ItemTake;
import Requests.Chat.Talk;
import Requests.Chat.TalkShout;
import Requests.Room.Wave;
import Requests.Staff.RoomTools;
import Requests.Staff.UserInfo;
import Requests.Staff.PickRoom;
import Requests.Inventory.Avatareffect.AvatarEffectSelected;
import Requests.Inventory.Furni.RequestRoomPropertySet;
import Requests.Navigator.CanCreateEvent;
import Requests.Poll.PollReject;
import Requests.Recycler.RecycleItems;
import Requests.Friendlist.SendRoomInvite;
import Requests.Sound.GetUserSongDisks;
import Requests.Sound.GetJukeboxPlayList;
import Requests.Room.Engine.UseFurniture;
import Requests.Room.MoodlightGet;
import Requests.Room.Pets.GetPetInfo;
import Requests.Inventory.Pets.GetPetInventory;
import Requests.Room.MoveWallItem;
import Requests.Room.Engine.PlacePet;
import Requests.Room.Pets.RespectPet;
import Requests.Room.MoodlightStatusUpdate;
import Requests.Room.Engine.RemovePetFromFlat;
import Requests.Room.MoodlightUpdate;
import Requests.Sound.RemoveJukeboxDisk;
import Requests.Poll.PollStart;
import Requests.Room.RoomEventEdit;
import Requests.Room.RoomEventStart;
import Requests.Room.RoomEventStop;
import Requests.Poll.PollAnswer;
import Requests.Poll.VoteAnswer;
import Requests.Chat.TalkWhisper;
import Requests.Room.UserRespect;
import Requests.Room.WiredData;
import Requests.Room.WiredDataConditionSave;
import Requests.Room.WiredDataEffectSave;
import Requests.Room.WiredDataTriggerSave;
import Requests.Room.RoomCreate;
import Requests.Catalog.GetCatalogIndex;
import Requests.Catalog.GetCatalogPage;
import Requests.Users.GetMOTD;
import Requests.Friendlist.SendMsg;
import Requests.Room.RoomListScoreRooms;
import Requests.Room.RoomListOwnRooms;
import Requests.Room.RoomListFriendsRooms;
import Requests.Room.RoomListFriendsInRooms;
import Requests.Room.RoomListByCatsRooms;
import Requests.Room.RoomListFavsRooms;
import Requests.Room.RoomListRecentRooms;
import Requests.Room.RoomListRoomEvents;
import Requests.Handshake.GetSessionParameters;
import Requests.Navigator.UpdateNavigatorSettings;
import Requests.Room.RoomTextSearch;
import Requests.Sound.SetSoundSettings;
import Requests.Staff.BanUser;
import Requests.Staff.ModKick;
import Requests.Staff.SendCaution;
import Requests.Staff.SendMessage;
import Requests.Tracking.EventLog;
import Requests.Friendlist.HabboSearch;
import Requests.Friendlist.RemoveBuddy;
import Requests.Tracking.PerformanceLog;
import Requests.Room.ResetUnseenItems;
import Requests.Handshake.SSOTicket;
import Requests.Inventory.Trading.OpenTrading;
import Requests.Navigator.GetOfficialRooms;
import Requests.Navigator.PublicSpaceCastLibs;
import Requests.Quest.FriendRequestQuest;
import Requests.Room.Action.AssignRights;
import Requests.Room.Action.KickUser;
import Requests.Room.Action.LetUserIn;
import Requests.Room.Engine.SetClothingChangeData;
import Requests.Room.Engine.UseWallItem;
import Requests.Room.Furniture.EnterOneWayDoor;
import Requests.Room.Furniture.PlacePostIt;
import Requests.Room.Roomsettings.DeleteRoom;
import Requests.Sound.GetNowPlaying;
import Requests.Users.GetUserNotifications;
import Requests.Friendlist.DeclineBuddy;

import Server.Catalog.CatalogPage;
import Server.ReadConfig.IniEditor;
import Server.Logging.Log;
import Server.Room.RoomManager;
import Server.Room.RoomListing;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Date;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/*
 *****************
 * @author capos *
 *****************
*/

public final class Server
{
    // Player Flags
    public static final int plrOnline = 1;
    public static final int plrPongOk = 1 << 1;
    public static final int plrMuted = 1 << 2;
    public static final int plrTeleporting = 1 << 3;

    // Room Flags
    public static final int rmIsNew = 1;
    public static final int rmBBanzai = 1 << 1;
    public static final int rmFBall = 1 << 2;
    public static final int rmStaffPickUp = 1 << 3;
    public static final int rmAllowPets = 1 << 4;
    public static final int rmAllowPetsEat = 1 << 5;
    public static final int rmAllowWalkthrough = 1 << 6;
    public static final int rmHideWall = 1 << 7;
    
    public Map<String, BannerGenerator> BannersRC4 = new HashMap<String, BannerGenerator>(100);
    public SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    public NavigatorCategories NavigatorCategories;
    public String[] DataBase = new String[3];
    private boolean DeveloperBuild = true;
    public RoomManager RoomManager;
    public RoomListing RoomListing;
    public Clients ClientManager;
    public Handler[] CallBacks;
    public Teleports Teleports;
    public Furniture Furniture;
    private int ItemIdCount;
    private int UserIdCount;
    private int RoomIdCount;
    public Catalog Catalog;
    public String MOTD;
    public Trax Trax;
    public Log Log;

    public Server(String CfgFile) throws Exception
    {
        boolean b = true;
        Object a = b;
        
        
        System.out.println("Starting Server..");
        InitLog();
        ReadConfig(CfgFile);
        InitClass();
        System.out.println("Server Started.");

        Thread t = new Thread(new ServerTasks(this));
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }
    
    public int GenerateRoomId()
    {
        return ++RoomIdCount;
    }

    public int GenerateItemId()
    {
        return ++ItemIdCount;
    }

    public int GenerateUserId()
    {
        return ++UserIdCount;
    }

    public int GetTimestamp()
    {
        return (int)(System.currentTimeMillis() / 1000);
    }

    Random ran = new Random( );
    public int GetRandomNumber(int i, int i2)
    {
        return i+ran.nextInt(i2);
    }

    private void ReadConfig(String CfgFile) throws Exception
    {
        System.out.println("Loading Config: "+CfgFile);
        IniEditor read = new IniEditor();
        read.load(CfgFile);

        int port = Integer.parseInt(read.get("ports", "gameport"));
        Thread t = new Thread(new Mus(Integer.parseInt(read.get("ports", "musport")),this));
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();

        System.out.println("Opening Game Port : "+port);
        final ServerBootstrap bootstrap = new ServerBootstrap
        (
                new NioServerSocketChannelFactory
                (
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool(),
                        Runtime.getRuntime().availableProcessors()*2
                )
        );
        
        final Server Env = this;

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new Connection(Env));
            }
        });

        final Channel ch = bootstrap.bind(new InetSocketAddress(port));
        /*
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setOption("child.connectTimeoutMillis", 250);
*/
        Thread shutdownThread = new Thread()
        {
                public void run( )
                {
                    System.out.println("Server: closing");
                    ch.close().awaitUninterruptibly();
                    bootstrap.releaseExternalResources();
                    System.out.println("Server: close");
                }
        };
        
        Runtime.getRuntime().addShutdownHook(shutdownThread);
        
        DataBase[0] = read.get("mysql", "dbname");
        DataBase[1] = read.get("mysql", "username");
        DataBase[2] = read.get("mysql", "password");
        
    }

    private void InitLog() throws Exception
    {
        Log = new Log(DeveloperBuild,date.format(new Date()));
    }

    private void InitClass() throws Exception
    {
        SimpleDateFormat date2 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        System.out.println(date2.format(new Date())+" - Loading Class Clients.. ");
        ClientManager = new Clients(this);
        System.out.println(date2.format(new Date())+" - Loading Class Furniture.. ");
        Furniture = new Furniture(DataBase);
        System.out.println(date2.format(new Date())+" - Loading Class Trax.. ");
        Trax = new Trax(DataBase);
        System.out.println(date2.format(new Date())+" - Loading Class Catalog.. ");
        Catalog = new Catalog(this);
        System.out.println(date2.format(new Date())+" - Loading Class RoomManager.. ");
        RoomManager = new RoomManager(this);
        System.out.println(date2.format(new Date())+" - Loading Class Teleports.. ");
        Teleports = new Teleports();
        System.out.println(date2.format(new Date())+" - Loading Class NavigatorCategories.. ");
        NavigatorCategories = new NavigatorCategories(DataBase);
        System.out.println(date2.format(new Date())+" - Loading Class RoomListing.. ");
        RoomListing = new RoomListing(NavigatorCategories.CategoriesCount());

        DatabaseClient DB = new DatabaseClient(DataBase);
        ResultSet query;
        
        query = DB.Query("SELECT id FROM `items` ORDER BY `id` DESC LIMIT 1;");
        if (!query.next()) return;
        ItemIdCount = query.getInt("id");
        query.close();
        
        query = DB.Query("SELECT id FROM `users` ORDER BY `id` DESC LIMIT 1;");
        if (!query.next()) return;
        UserIdCount = query.getInt("id");
        query.close();
        
        query = DB.Query("SELECT id FROM `rooms` ORDER BY `id` DESC LIMIT 1;");
        if (!query.next()) return;
        RoomIdCount = query.getInt("id");
        query.close();
        
        query = DB.Query("SELECT message FROM motd LIMIT 1;");
        if (!query.next()) return;
        MOTD = query.getString("message");
        query.close();

        DB.Close();
        
        try
        {
            generateCatalogIndex();
        }
        catch (Exception ex)
        {
            Log.Print(ex);
        }

        System.out.println(date2.format(new Date())+" - Loading CallBacks");
        CallBacks();
    }

    public void CallBacks() throws Exception
    {
        CallBacks = new Handler[4096];
        RegCallBack(7, new InfoRetrieve());
        RegCallBack(8, new CreditsBalance());
        RegCallBack(12, new MessengerInit());
        RegCallBack(15, new FriendListUpdate());
        RegCallBack(19, new AddFavouriteRoom());
        RegCallBack(20, new DeleteFavouriteRoom());
        RegCallBack(23, new DeleteRoom());
        RegCallBack(26, new ScrGetUserInfo());
        RegCallBack(29, new RoomCreate());
        RegCallBack(33, new SendMsg());
        RegCallBack(34, new SendRoomInvite());
        RegCallBack(37, new AcceptBuddy());
        RegCallBack(38, new DeclineBuddy());
        RegCallBack(39, new RequestBuddy());
        RegCallBack(40, new RemoveBuddy());
        RegCallBack(41, new HabboSearch());
        RegCallBack(42, new ApproveName());
        RegCallBack(44, new LookUpdate());
        RegCallBack(52, new Talk());
        RegCallBack(55, new TalkShout());
        RegCallBack(56, new TalkWhisper());
        RegCallBack(53, new GoToHotelView());
        RegCallBack(66, new RequestRoomPropertySet());
        RegCallBack(67, new ItemTake());
        RegCallBack(71, new OpenTrading());
        RegCallBack(73, new MoveFloorItem());
        RegCallBack(75, new Move());
        RegCallBack(79, new LookAt());
        RegCallBack(90, new ItemPlace());
        RegCallBack(91, new MoveWallItem());
        RegCallBack(93, new Dance());
        RegCallBack(94, new Wave());
        RegCallBack(95, new KickUser());
        RegCallBack(96, new AssignRights());
        RegCallBack(98, new LetUserIn());
        RegCallBack(100, new PurchaseFromCatalog());
        RegCallBack(101, new GetCatalogIndex());
        RegCallBack(102, new GetCatalogPage());
        RegCallBack(112, new VoteAnswer());
        RegCallBack(126, new RoomData3());
        RegCallBack(151, new GetUserFlatCats());
        RegCallBack(157, new GetBadges());
        RegCallBack(158, new SetActivatedBadges());
        RegCallBack(159, new GetSelectedBadges());
        RegCallBack(182, new GetInterstitial());
        RegCallBack(196, new Pong());
        RegCallBack(206, new InitCrypto());
        RegCallBack(215, new RoomData1());
        RegCallBack(221, new GetSongInfo());
        RegCallBack(228, new GetSoundSettings());
        RegCallBack(229, new SetSoundSettings());
        RegCallBack(230, new GroupBadgesGet());
        RegCallBack(232, new EnterOneWayDoor());
        RegCallBack(233, new GetBuddyRequests());
        RegCallBack(234, new PollStart());
        RegCallBack(235, new PollReject());
        RegCallBack(236, new PollAnswer());
        RegCallBack(249, new GetNowPlaying());
        RegCallBack(255, new AddJukeboxDisk());
        RegCallBack(256, new RemoveJukeboxDisk());
        RegCallBack(259, new GetUserSongDisks());
        RegCallBack(258, new GetJukeboxPlayList());
        RegCallBack(261, new RateRoomGive());
        RegCallBack(262, new FollowFriend());
        RegCallBack(263, new UserTagsGet());
        RegCallBack(315, new LatencyPingRequest());
        RegCallBack(316, new LatencyPingReport());
        RegCallBack(317, new TypingStart());
        RegCallBack(318, new TypingStop());
        RegCallBack(321, new GetIgnoredUsers());
        RegCallBack(341, new MoodlightGet());
        RegCallBack(342, new MoodlightUpdate());
        RegCallBack(343, new MoodlightStatusUpdate());
        RegCallBack(345, new CanCreateEvent());
        RegCallBack(346, new RoomEventStart());
        RegCallBack(347, new RoomEventStop());
        RegCallBack(348, new RoomEventEdit());
        RegCallBack(370, new GetAchievements());
        RegCallBack(371, new UserRespect());
        RegCallBack(372, new AvatarEffectSelected());
        RegCallBack(373, new AvatarEffectActivated());
        RegCallBack(375, new WardrobeGet());
        RegCallBack(376, new WardrobeSave());
        RegCallBack(380, new GetOfficialRooms());
        RegCallBack(382, new RoomPopularTags());
        RegCallBack(384, new UpdateNavigatorSettings());
        RegCallBack(385, new RoomInfo());
        RegCallBack(387, new RoomCanCreate());
        RegCallBack(390, new GetRoomEntryData());
        RegCallBack(391, new OpenFlatConnection());
        RegCallBack(392, new UseFurniture());
        RegCallBack(393, new UseWallItem());
        RegCallBack(400, new GetRoomSettings());
        RegCallBack(401, new SaveRoomSettings());
        RegCallBack(404, new RequestFurniInventory());
        RegCallBack(412, new GetRecyclerPrizes());
        RegCallBack(413, new GetRecyclerStatus());
        RegCallBack(414, new RecycleItems());
        RegCallBack(415, new SSOTicket());
        RegCallBack(421, new PerformanceLog());
        RegCallBack(430, new RoomListByCatsRooms());
        RegCallBack(431, new RoomListScoreRooms());
        RegCallBack(432, new RoomListFriendsRooms());
        RegCallBack(433, new RoomListFriendsInRooms());
        RegCallBack(434, new RoomListOwnRooms());
        RegCallBack(435, new RoomListFavsRooms());
        RegCallBack(436, new RoomListRecentRooms());
        RegCallBack(437, new RoomTextSearch());
        RegCallBack(439, new RoomListRoomEvents());
        RegCallBack(453, new PublicSpaceCastLibs());
        RegCallBack(454, new UserInfo());
        RegCallBack(459, new RoomTools());
        RegCallBack(461, new SendCaution());
        RegCallBack(462, new SendMessage());
        RegCallBack(463, new ModKick());
        RegCallBack(464, new BanUser());
        RegCallBack(473, new GetGiftWrappingConfiguration());
        RegCallBack(474, new GetClubGiftInfo());
        RegCallBack(480, new SetClothingChangeData());
        RegCallBack(482, new EventLog());
        RegCallBack(483, new PickRoom());
        RegCallBack(484, new MottoUpdate());
        RegCallBack(490, new FindNewFriends());
        RegCallBack(512, new Disconnect());
        RegCallBack(813, new UniqueID());
        RegCallBack(1170, new VersionCheck());
        RegCallBack(1817, new GetSessionParameters());
        RegCallBack(2002, new GenerateSecretKey());
        RegCallBack(3000, new GetPetInventory());
        RegCallBack(3001, new GetPetInfo());
        RegCallBack(3002, new PlacePet());
        RegCallBack(3003, new RemovePetFromFlat());
        RegCallBack(3004, new GetPetCommands());
        RegCallBack(3005, new RespectPet());
        RegCallBack(3007, new GetSellablePetBreeds());
        RegCallBack(3011, new GetMarketplaceConfiguration());
        RegCallBack(3012, new GetMarketplaceCanMakeOffer());
        RegCallBack(3030, new GetIsOfferGiftable());
        RegCallBack(3031, new GetClubOffers());
        RegCallBack(3032, new GetBadgePointLimits());
        RegCallBack(3036, new ExtendSubscriptionPromo());
        RegCallBack(3050, new WiredDataTriggerSave());
        RegCallBack(3051, new WiredDataEffectSave());
        RegCallBack(3052, new WiredDataConditionSave());
        RegCallBack(3053, new WiredData());
        RegCallBack(3105, new GetUserNotifications());
        RegCallBack(3110, new GetMOTD());
        RegCallBack(3111, new ResetUnseenItems());
        RegCallBack(3210, new FriendRequestQuest());
        RegCallBack(3254, new PlacePostIt());
    }

    private void RegCallBack(int MsgId, Handler hdlr) throws Exception
    {
        CallBacks[MsgId] = hdlr;
    }
    
    public void InitPacket(int Header, ServerMessage Message)
    {        
        try
        {
            Message.Ready= false;
            Message.bufferout.clear();
            Message.output.writeInt(0); // packet len
            Message.output.writeShort(Header);
            System.out.print("New Packet ID > ("+Header);
        }
        catch (Exception ex)
        {
            Log.Print(ex);
        }
    }

    public void EndPacket(Channel Client, ServerMessage Message)
    {
        if(!Message.Ready)
        {
            Message.bufferout.setInt(0, Message.bufferout.writerIndex() - 4);
            Message.Ready= true;
        }
        
        Client.write(Message.bufferout);
        System.out.println(") Sended!");
    }
    
    public void Append(Object add, ServerMessage Message)
    {
        try
        {
            if (add instanceof Integer)
            {
                Message.output.writeInt((Integer)add);
            }
            else if(add instanceof String)
            {
                Message.output.writeUTF((String)add);
            }
            else if (add instanceof Boolean)
            {
                Message.output.writeBoolean((Boolean)add);
            }
            System.out.print(add.getClass()+",");
        }
        catch (Exception ex)
        {
            Log.Print(ex);
        }
    }
    
    public ServerMessage CatalogIndex;
    
    private void generateCatalogTab(int Tab)
    {
        List<CatalogPage> a = Catalog.CatalogMap.get(Tab);
        Append(a.size(),CatalogIndex);
        for(CatalogPage Page : a)
        {
            Append(Page.Visible,CatalogIndex);
            Append(Page.IconColor,CatalogIndex);
            Append(Page.IconImage,CatalogIndex);
            Append(Page.Id,CatalogIndex);
            Append(Page.Caption,CatalogIndex);
            if(Catalog.CatalogMap.containsKey(Page.Id))
            {
                generateCatalogTab(Page.Id);
            }
            else
            {
                Append(0,CatalogIndex);
            }
        }
    }
    
    private void generateCatalogIndex()
    {
        CatalogIndex = new ServerMessage();
        
        InitPacket(126, CatalogIndex);
        Append(true, CatalogIndex);
        Append(0, CatalogIndex);
        Append(0, CatalogIndex);
        Append(-1, CatalogIndex);
        Append("", CatalogIndex);
        generateCatalogTab(-1);
        Append(false, CatalogIndex); // CATALOG_NEW_ITEMS_SHOW (new_additions.notification)

    }
    
    public int b64Decode(String s)
    {
        int intTot = 0;
        int y = 0;
        for (int x = (s.length() - 1); x >= 0; x--)
        {
            int intTmp = s.charAt(x) - 64;
            if (y > 0)
                intTmp *= (int)Math.pow(64, y);
            intTot += intTmp;
            y++;
        }
        return intTot;
    }

    public String b64Encode(int i)
    {
        String s = "";

        for (int x = 1; x <= 2; x++)
            s += (char)((byte)(64 + (i >> 6 * (2 - x) & 63)));

        return s;
    }

    public void BuyVipOrClub(Connection Main, int ToAdd, int Type)
    {
        int TimeToAdd = ToAdd*2678400; // 31 days * 24 hours * 60 minutes * 60 seconds

        if(Main.CheckSubscription()) Main.Data.Subscription.TimeExpire += TimeToAdd;
        else Main.Data.Subscription.TimeExpire = GetTimestamp() + TimeToAdd;
        Main.Data.Subscription.Type = Type;

        SerializeSubscription(Main,2);

        if(Type==1)
        {
            Main.Data.TotalLengthHC += ToAdd;
        }
        else
        {
            Main.Data.TotalLengthVIP += ToAdd;
            Main.Data.MaxRooms = 20;
        }

        InitPacket(2,Main.ClientMessage);
        Append(Main.Data.Subscription.Type,Main.ClientMessage);
        Append(Main.Data.RankLevel,Main.ClientMessage);
        EndPacket(Main.Socket, Main.ClientMessage);

        Main.GiveBadge("HC1");
    }

    public void SerializeSubscription(Connection Main, int Type)
    {
        int TimeLeft = Main.Data.Subscription.TimeExpire - GetTimestamp();
        if(TimeLeft<0) TimeLeft = 0;
        int TotalDaysLeft = (TimeLeft / 86400); // 86400 = 60 sec * 60 min * 24 hours

        InitPacket(7,Main.ClientMessage);
        Append("habbo_club",Main.ClientMessage);
        Append(TotalDaysLeft % 31,Main.ClientMessage); // Days left
        Append(1,Main.ClientMessage); // int - no se usa
        Append(TotalDaysLeft / 31,Main.ClientMessage); // Months Left
        Append(Type,Main.ClientMessage); // TYPE (2 = buy , 1 y 3 no se para que sean)
        Append(true,Main.ClientMessage); // clubHasEverBeenMember
        Append(Main.Data.Subscription.Type > 1,Main.ClientMessage); // vip
        Append(Main.Data.TotalLengthHC,Main.ClientMessage);
        Append(Main.Data.TotalLengthVIP,Main.ClientMessage);
        Append(false,Main.ClientMessage); // promotion (discount..)
        Append(100,Main.ClientMessage); // regular price (used if promotion is active)
        Append(50,Main.ClientMessage); // your price (used if promotion is active)
        EndPacket(Main.Socket, Main.ClientMessage);
    }


    public boolean ValidateLook(String SelectedLook)
    {
        for(String Part : SelectedLook.split("."))
        {
            String[] Set = Part.split("-");
            if (Set.length > 0)
            {
                if(IsBadType(Set[0]))
                {
                    return false;
                }

                if(IsBadInteger(Set[1]))
                {
                    return false;
                }

                int a = 2;
                while (a < Set.length)
                {
                    if(IsBadInteger(Set[a]))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean IsBadInteger(String i)
    {
        try
        {
            Integer.parseInt(i);
            return false;
        }
        catch(NumberFormatException ex)
        {
            return true;
        }
    }

    private static final String[] Sets = {"hd","hr","ha","he","ea","fa","cc","ch","ca","cp","lg","sh","wa"};

    private boolean IsBadType(String string)
    {
        for(String Set : Sets)
        {
            if(Set.equals(string))
            {
                return false;
            }
        }
        return true;
    }

}
