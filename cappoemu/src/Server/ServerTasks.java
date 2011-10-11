package Server;

import Server.Room.Room;

/*
 *****************
 * @author capos *
 *****************
*/

public class ServerTasks extends Thread
{
    private static final int MAX_ROOMS = 30;
    private Server Environment;
    private int catcount;

    public ServerTasks(Server Env)
    {
        Environment = Env;
        catcount = Environment.NavigatorCategories.CategoriesCount();
    }

    public void GenMostPopular()
    {
        Object[] keys = Environment.RoomManager.GetRooms().values().toArray();

        Room[] Rooms=new Room[keys.length];int[]Users=new int[keys.length];

        int row = 0;
        for (int a = 0; a < keys.length; a++)
        {
            Room Current = (Room) keys[a];
            if(Current!=null && Current.UsersNow>0)
            {
                Rooms[row] = Current;
                Users[row++] = Current.UsersNow;
                continue;
            }
        }

        int len = row;

        int t;Room t2;
        for (int i=0;i<len;i++)
        {
            for (int j=i;j>0&&Users[j-1]<Users[j];j--)
            {
                t=Users[j];
                Users[j]=Users[j-1];
                Users[j-1]=t;
                t2=Rooms[j];
                Rooms[j]=Rooms[j-1];
                Rooms[j-1]=t2;
            }
        }

        if(len>MAX_ROOMS)
        {
            len = MAX_ROOMS;
        }

        try{sleep(10000);}catch (Exception ex){}

        ServerMessage MsgRooms = Environment.RoomListing.GetPopular();
        Environment.RoomListing.PopularActived(false);
        Environment.InitPacket(451, MsgRooms);
        Environment.Append(-1, MsgRooms);
        Environment.Append(Integer.toString(1), MsgRooms);
        Environment.Append(len, MsgRooms);
        for(int i=0;i<len;i++)
        {
            Environment.RoomManager.SerializeRoom(MsgRooms,Rooms[i],Users[i],0);
        }
        Environment.Append(false, MsgRooms);
        Environment.RoomListing.PopularActived(true);
    }

    public void GenMostPopularByCat(int Category)
    {
        Object[] keys = Environment.RoomManager.GetRooms().values().toArray();

        Room[] Rooms=new Room[keys.length];int[]Users=new int[keys.length];

        int row = 0;
        for (int a = 0; a < keys.length; a++)
        {
            Room Current = (Room) keys[a];
            if(Current!=null && Current.Category == Category && Current.UsersNow > 0)
            {
                Rooms[row] = Current;
                Users[row++] = Current.UsersNow;
                continue;
            }
        }

        int len = row;

        int t;Room t2;
        for (int i=0;i<len;i++)
        {
            for (int j=i;j>0&&Users[j-1]<Users[j];j--)
            {
                t=Users[j];
                Users[j]=Users[j-1];
                Users[j-1]=t;
                t2=Rooms[j];
                Rooms[j]=Rooms[j-1];
                Rooms[j-1]=t2;
            }
        }

        if(len>MAX_ROOMS)
        {
            len = MAX_ROOMS;
        }

        try{sleep(10000);}catch (Exception ex){}

        
        ServerMessage MsgRooms = Environment.RoomListing.GetByCat(Category);
        Environment.RoomListing.ByCatActived(Category, false);
        Environment.InitPacket(451, MsgRooms);
        Environment.Append(Category, MsgRooms);
        Environment.Append(Integer.toString(1), MsgRooms);
        Environment.Append(len, MsgRooms);
        for(int i = 0;i<len;i++)
        {
            Environment.RoomManager.SerializeRoom(MsgRooms,Rooms[i],Users[i],0);
        }
        Environment.Append(false, MsgRooms);
        Environment.RoomListing.ByCatActived(Category, true);
    }


    public void GenMostScore()
    {
        Object[] keys = Environment.RoomManager.GetRooms().values().toArray();

        Room[] Rooms=new Room[keys.length];int[]Scores=new int[keys.length];

        int row = 0;
        for (int a = 0; a < keys.length; a++)
        {
            Room Current = (Room) keys[a];
            if(Current!=null && Current.Score > 0)
            {
                Rooms[row] = Current;
                Scores[row++] = Current.Score;
                continue;
            }
        }

        int len = row;

        int t;Room t2;
        for (int i=0;i<len;i++)
        {
            for (int j=i;j>0&&Scores[j-1]<Scores[j];j--)
            {
                t=Scores[j];
                Scores[j]=Scores[j-1];
                Scores[j-1]=t;
                t2=Rooms[j];
                Rooms[j]=Rooms[j-1];
                Rooms[j-1]=t2;
            }
        }

        if(len>MAX_ROOMS)
        {
            len = MAX_ROOMS;
        }

        try{sleep(10000);}catch (Exception ex){}

        
        ServerMessage MsgRooms = Environment.RoomListing.GetScore();
        Environment.RoomListing.ScoreActived(false);
        Environment.InitPacket(451, MsgRooms);
        Environment.Append(0, MsgRooms);
        Environment.Append(Integer.toString(2), MsgRooms);
        Environment.Append(len, MsgRooms);
        for(int i = 0;i<len;i++)
        {
            Environment.RoomManager.SerializeRoom(MsgRooms,Rooms[i],0,Scores[i]);
        }
        Environment.Append(false, MsgRooms);
        Environment.RoomListing.ScoreActived(true);
    }

    @Override
    public void run()
    {
        do
        {
            try{sleep(10000);}catch (Exception ex){}
            GenMostPopular();
            try{sleep(10000);}catch (Exception ex){}
            for(int i =0;i<catcount;i++)
            {
                GenMostPopularByCat(i);
                try{sleep(10000);}catch (Exception ex){}
            }
            try{sleep(10000);}catch (Exception ex){}
            GenMostScore();
            try{sleep(10000);}catch (Exception ex){}

            //int UsersLoaded = Environment.ClientManager.GetLoadedCount();
            int UsersOnline = Environment.ClientManager.GetOnlineCount();
           // int RoomsLoaded = Environment.RoomManager.GetLoadedCount();
           // int RoomsActive = Environment.RoomManager.GetActiveCount();

            DatabaseClient DB;
            try
            {
                DB = new DatabaseClient(Environment.DataBase);
            }
            catch (Exception ex)
            {
                continue;
            }
            try
            {
                DB.Update("UPDATE `stats` SET `online` = "+UsersOnline+";");
            }
            catch (Exception ex) { }

            DB.Close();
            
            // Clean....
            
            System.gc();
            
            try{sleep(10000);}catch (Exception ex){}
            
            int notused = Environment.GetTimestamp()-3600; // 1 hour
            
            if(true) // Clean Unused Data Clients
            {
                Object[] keys = Environment.ClientManager.GetClients().values().toArray();
            
                int loops = (keys.length / 1000) + 1;
                int p = 0;
                while(--loops>=0)
                {
                    int end = p + 1000;
                    if(end > keys.length)
                    {
                        end = keys.length;
                    }
                    
                    for (int a = p; a < end; a++)
                    {
                        Player Current = (Player)keys[a];
                        if(Current==null) continue;
                        if(Current.LastUsedThis > notused) continue; // used in last hour
                        if((Current.Flags & Server.plrOnline) == Server.plrOnline) continue;

                        Environment.ClientManager.DeleteID(Current.Id);
                    }
                    
                    p = end;
                    
                    try{sleep(5000);}catch (Exception ex){}
                }
            }
        }
        while(true);
    }
}