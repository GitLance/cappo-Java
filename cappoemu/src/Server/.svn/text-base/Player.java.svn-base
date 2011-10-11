package Server;

import Server.Room.RoomUser;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/*
 *****************
 * @author capos *
 *****************
*/

public class Player extends Thread
{
    public int Flags;
    public String UserName;
    public String Motto;
    public String Look;
    public String RealName;
    public String LastVisit;
    public int Sex;
    public int Volumen = 100;
    public int Id = 0;
    public int TeleporterId = 0;
    public int AchievementsScore;
    public int Credits;
    public int Pixels;
    public int Shells;
    public int Respects;
    public int CurrentRoom = 0;
    public int LoadingRoom = 0;
    public int DailyRespectPoints;
    public int DailyPetRespectPoints;
    public int AccessCount;
    public int HomeRoom;
    public int TotalLengthHC;
    public int TotalLengthVIP;
    public int MaxRooms;
    public int MaxFriends;
    public int RankLevel;
    public int CautionCount;
    public int EcotronNextTime;
    public List<Integer> Friends = new ArrayList<Integer>();
    public List<String> FriendCategories= new ArrayList<String>();
    public List<Integer> Friend_Requests = new ArrayList<Integer>();
    public Map<Integer, UserItem> InventoryItems = new HashMap<Integer, UserItem>();
    public Map<Integer, Integer> SongInInventory = new HashMap<Integer, Integer>();
    public Map<Integer, UserItem> InventoryItemsWall = new HashMap<Integer, UserItem>();
    public Map<Integer, Pet> InventoryPets = new HashMap<Integer, Pet>();
    public Map<Integer, Integer> Achievements = new HashMap<Integer, Integer>();
    public Map<Integer, String> Wardrobes = new HashMap<Integer, String>();
    public List<Integer> Favorite_Rooms = new ArrayList<Integer>();
    public List<Integer> FriendsUpdateNeeded = new ArrayList<Integer>();
    public List<String> Tags = new ArrayList<String>();
    public List<Badge> Badges = new ArrayList<Badge>();
    public List<Badge> BadgesSelected = new ArrayList<Badge>();
    public List<AvatarEffect> Effects = new ArrayList<AvatarEffect>();
    public List<Integer> RatedRooms = new ArrayList<Integer>();
    public Subscription Subscription;
    public List<Integer> OwnRooms = new ArrayList<Integer>();

    public RoomUser RoomUser;
    public Trade Trade;

    public Connection Connection;
    public int LastUsedThis;

    public final void SetFlag(int Flag, boolean Add)
    {
        if(Add)
        {
            if((Flags & Flag) != Flag)
            {
                Flags |= Flag;
            }
        }
        else
        {
            Flags &= ~Flag;
        }
    }

    public boolean IsStaff()
    {
        return (RankLevel>0);
    }
}