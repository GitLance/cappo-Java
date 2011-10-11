package Server;

import Server.Server;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/*
 *****************
 * @author capos *
 *****************
*/

public class Clients
{
    private Server Environment;
    private Map<Integer, Player> Clients = new HashMap<Integer, Player>(10000,0.9f);
    private Map<String, Player> ClientsByName = new HashMap<String, Player>(10000,0.9f);
    private int OnlineUsers;

    public Clients(Server Env)
    {
        Environment = Env;
    }

    public Player GetClient(int ClientId)
    {        
        if(Clients.containsKey(ClientId))
        {
            Player p = Clients.get(ClientId);
            p.LastUsedThis = Environment.GetTimestamp();
            return p;
        }
        else
        {
            return LoadHabbo(ClientId);
        }
    }

    public Player GetClientByName(String UserName)
    {
        if(ClientsByName.containsKey(UserName))
        {
            return ClientsByName.get(UserName);
        }

        return LoadHabboByName(UserName);
    }

    public void AddID(Player Client)
    {
        Client.LastUsedThis = Environment.GetTimestamp();
        Clients.put(Client.Id, Client);
        ClientsByName.put(Client.UserName, Client);
    }
    
    public void DeleteID(int ClientId)
    {
        Clients.remove(ClientId);
    }
    
    public Map<Integer, Player> GetClients()
    {
        return Clients;
    }

    public int GetOnlineCount()
    {
        return OnlineUsers;
    }

    public int GetLoadedCount()
    {
        return Clients.size();
    }

    public void SetOnline(boolean online)
    {
        if(online)
        {
            OnlineUsers++;
        }
        else
        {
            OnlineUsers--;
        }
    }

    private Player LoadHabbo(int Id)
    {
        DatabaseClient DB;
        try {
            DB = new DatabaseClient(Environment.DataBase);
        }
        catch (Exception ex)
        {
            return null;
        }

        try
        {
            ResultSet userdata = DB.Query("SELECT * FROM users WHERE id = "+Id+" LIMIT 1;");

            if(userdata.next())
            {
                Player Main = new Player();

                Main.Id = userdata.getInt("id");
                Main.AccessCount = userdata.getInt("access_count") + 1;
                Main.UserName = userdata.getString("username");
                Main.RealName = userdata.getString("realname");
                Main.RankLevel = userdata.getInt("rank");
                Main.Look = userdata.getString("look");
                Main.Sex = userdata.getInt("sex");
                Main.Motto = userdata.getString("mission");
                Main.Credits = userdata.getInt("credits");
                Main.Pixels = userdata.getInt("pixels");
                Main.Shells = userdata.getInt("shells");
                Main.LastVisit = Environment.date.format(new Date());
                Main.Respects = userdata.getInt("respects");
                Main.DailyRespectPoints = userdata.getInt("have_normal_respects");
                Main.DailyPetRespectPoints = userdata.getInt("have_pet_respects");
                Main.TotalLengthHC = userdata.getInt("totallength_hc");
                Main.TotalLengthVIP = userdata.getInt("totallength_vip");
                Main.Subscription = new Subscription();
                Main.Subscription.Type = userdata.getInt("subscription_type");
                Main.Subscription.TimeExpire = userdata.getInt("subscription_timestamp");
                Main.AchievementsScore = 10 * Main.Achievements.size();
                String Friendships = userdata.getString("friends");
                while(Friendships.length() > 2)
                {
                    int len = Environment.b64Decode(Friendships.substring(0,2));
                    Friendships = Friendships.substring(2);
                    Main.Friends.add(Integer.parseInt(Friendships.substring(0,len)));
                    Friendships = Friendships.substring(len);
                }
                String Friendscategories = userdata.getString("friendcategories");
                while(Friendscategories.length() > 2)
                {
                    int len = Environment.b64Decode(Friendscategories.substring(0,2));
                    Friendscategories = Friendscategories.substring(2);
                    Main.FriendCategories.add(Friendscategories.substring(0,len));
                    Friendscategories = Friendscategories.substring(len);
                }
                String Friendrequest = userdata.getString("friendrequests");
                while(Friendrequest.length() > 2)
                {
                    int len = Environment.b64Decode(Friendrequest.substring(0,2));
                    Friendrequest = Friendrequest.substring(2);
                    Main.Friend_Requests.add(Integer.parseInt(Friendrequest.substring(0,len)));
                    Friendrequest = Friendrequest.substring(len);
                }
                Main.HomeRoom = userdata.getInt("home_room");
                String MyRooms = userdata.getString("ownrooms");
                while(MyRooms.length() > 2)
                {
                    int len = Environment.b64Decode(MyRooms.substring(0,2));
                    MyRooms = MyRooms.substring(2);
                    Main.OwnRooms.add(Integer.parseInt(MyRooms.substring(0,len)));
                    MyRooms = MyRooms.substring(len);
                }
                String SalasFavoritas = userdata.getString("salas_favoritas");
                while(SalasFavoritas.length() > 2)
                {
                    int len = Environment.b64Decode(SalasFavoritas.substring(0,2));
                    SalasFavoritas = SalasFavoritas.substring(2);
                    Main.Favorite_Rooms.add(Integer.parseInt(SalasFavoritas.substring(0,len)));
                    SalasFavoritas = SalasFavoritas.substring(len);
                }
                String Badges = userdata.getString("badges");
                while(Badges.length() > 2)
                {
                    int len = Environment.b64Decode(Badges.substring(0,2));
                    Badges = Badges.substring(2);
                    String BadgeCode = Badges.substring(0,len);
                    Badges = Badges.substring(len);
                    int Slot = Environment.b64Decode(Badges.substring(0,2));
                    Badges = Badges.substring(2);

                    Badge badge = new Badge(BadgeCode, Slot);
                    Main.Badges.add(badge);
                    if(Slot>0)
                    {
                        Main.BadgesSelected.add(badge);
                    }
                }
                if(Main.Subscription.Type == 2)
                {
                    Main.MaxRooms = 20;
                    Main.MaxFriends = 100;
                }
                else
                {
                    Main.MaxRooms = 5;
                    if(Main.Subscription.Type == 1) Main.MaxFriends = 50;
                    else Main.MaxFriends = 25;
                }

                AddID(Main);

                userdata.close();
                DB.Close();

                return Main;
            }
        }
        catch (Exception ex)
        {

        }

        try
        {
            DB.Close();
        }
        catch(Exception ex)
        {
        }

        return null;
    }


    private Player LoadHabboByName(String Name)
    {
        DatabaseClient DB;
        try {
            DB = new DatabaseClient(Environment.DataBase);
        }
        catch (Exception ex)
        {
            return null;
        }

        try
        {
            ResultSet userdata = DB.SecureQuery("SELECT * FROM users WHERE username = ? LIMIT 1;",Name);

            if(userdata.next())
            {
                Player Main = new Player();

                Main.Id = userdata.getInt("id");
                Main.AccessCount = userdata.getInt("access_count") + 1;
                Main.UserName = userdata.getString("username"); // need username for client manager..
                Main.RealName = userdata.getString("realname");
                Main.RankLevel = userdata.getInt("rank");
                Main.Look = userdata.getString("look");
                Main.Sex = userdata.getInt("sex");
                Main.Motto = userdata.getString("mission");
                Main.Credits = userdata.getInt("credits");
                Main.Pixels = userdata.getInt("pixels");
                Main.Shells = userdata.getInt("shells");
                Main.LastVisit = Environment.date.format(new Date());
                Main.Respects = userdata.getInt("respects");
                Main.DailyRespectPoints = userdata.getInt("have_normal_respects");
                Main.DailyPetRespectPoints = userdata.getInt("have_pet_respects");
                Main.TotalLengthHC = userdata.getInt("totallength_hc");
                Main.TotalLengthVIP = userdata.getInt("totallength_vip");
                Main.Subscription = new Subscription();
                Main.Subscription.Type = userdata.getInt("subscription_type");
                Main.Subscription.TimeExpire = userdata.getInt("subscription_timestamp");
                Main.AchievementsScore = 10 * Main.Achievements.size();
                String Friendships = userdata.getString("friends");
                while(Friendships.length() > 2)
                {
                    int len = Environment.b64Decode(Friendships.substring(0,2));
                    Friendships = Friendships.substring(2);
                    Main.Friends.add(Integer.parseInt(Friendships.substring(0,len)));
                    Friendships = Friendships.substring(len);
                }
                String Friendscategories = userdata.getString("friendcategories");
                while(Friendscategories.length() > 2)
                {
                    int len = Environment.b64Decode(Friendscategories.substring(0,2));
                    Friendscategories = Friendscategories.substring(2);
                    Main.FriendCategories.add(Friendscategories.substring(0,len));
                    Friendscategories = Friendscategories.substring(len);
                }
                String Friendrequest = userdata.getString("friendrequests");
                while(Friendrequest.length() > 2)
                {
                    int len = Environment.b64Decode(Friendrequest.substring(0,2));
                    Friendrequest = Friendrequest.substring(2);
                    Main.Friend_Requests.add(Integer.parseInt(Friendrequest.substring(0,len)));
                    Friendrequest = Friendrequest.substring(len);
                }
                Main.HomeRoom = userdata.getInt("home_room");
                String MyRooms = userdata.getString("ownrooms");
                while(MyRooms.length() > 2)
                {
                    int len = Environment.b64Decode(MyRooms.substring(0,2));
                    MyRooms = MyRooms.substring(2);
                    Main.OwnRooms.add(Integer.parseInt(MyRooms.substring(0,len)));
                    MyRooms = MyRooms.substring(len);
                }
                String SalasFavoritas = userdata.getString("salas_favoritas");
                while(SalasFavoritas.length() > 2)
                {
                    int len = Environment.b64Decode(SalasFavoritas.substring(0,2));
                    SalasFavoritas = SalasFavoritas.substring(2);
                    Main.Favorite_Rooms.add(Integer.parseInt(SalasFavoritas.substring(0,len)));
                    SalasFavoritas = SalasFavoritas.substring(len);
                }
                if(Main.Subscription.Type == 2)
                {
                    Main.MaxRooms = 20;
                    Main.MaxFriends = 100;
                }
                else
                {
                    Main.MaxRooms = 5;
                    if(Main.Subscription.Type == 1) Main.MaxFriends = 50;
                    else Main.MaxFriends = 25;
                }

                AddID(Main);

                userdata.close();
                DB.Close();

                return Main;
            }
        }
        catch (Exception ex)
        {
        }

        try
        {
            DB.Close();
        }
        catch(Exception ex)
        {
        }

        return null;
    }

}
