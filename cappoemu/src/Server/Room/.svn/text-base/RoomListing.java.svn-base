package Server.Room;

import Server.ServerMessage;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomListing
{
    ServerMessage PopularRooms = new ServerMessage();
    ServerMessage ScoreRooms = new ServerMessage();
    ServerMessage[] ByCatRooms;

    private boolean state1 = true;
    private boolean state2 = true;
    private boolean[] state3;
    
    public RoomListing(int CatCount)
    {
        ByCatRooms = new ServerMessage[CatCount];
        state3 = new boolean[CatCount];
        for(int i = 0;i<CatCount;i++)
        {
            ByCatRooms[i] = new ServerMessage();
            state3[i] = true;
        }
    }

    public void PopularActived(boolean state)
    {
        state1 = state;
    }
    public ServerMessage GetPopular()
    {
        return (state1)?PopularRooms:null;
    }

    public void ScoreActived(boolean state)
    {
        state2 = state;
    }
    public ServerMessage GetScore()
    {
        return (state2)?ScoreRooms:null;
    }

    public void ByCatActived(int Category, boolean state)
    {
        state3[Category] = state;
    }
    public ServerMessage GetByCat(int Category)
    {
        return (state3[Category])?ByCatRooms[Category]:null;
    }
}