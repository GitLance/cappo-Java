package Server;

import java.util.HashMap;
import java.util.Map;

/*
 *****************
 * @author capos *
 *****************
*/

public class Teleports
{
    private Map<Integer,Integer> ParentId = new HashMap<Integer,Integer>();
    private Map<Integer,Integer> RoomId = new HashMap<Integer,Integer>();

    public void SetParents(int Id1, int Id2)
    {
        ParentId.put(Id1,Id2);
        ParentId.put(Id2,Id1);
    }

    public void SetRoom(int Id, int Room_Id)
    {
        RoomId.put(Id,Room_Id);
    }

    public int GetTele(int Id)
    {
        if(ParentId.containsKey(Id))
        {
            return ParentId.get(Id);
        }
        return -1;
    }

    public int GetRoom(int Id)
    {
        if(RoomId.containsKey(Id))
        {
            return RoomId.get(Id);
        }
        return -1;
    }

    public void DelRoom(int Id)
    {
        RoomId.remove(Id);
    }

}
