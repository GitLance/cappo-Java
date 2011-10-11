package Server;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/*
 *****************
 * @author capos *
 *****************
*/

public class Trax
{
    public Map<Integer, TraxDisc> Discs = new HashMap<Integer, TraxDisc>();

    public Trax (String[] DATA) throws Exception
    {
        DatabaseClient DB = new DatabaseClient(DATA);
        ResultSet table = DB.Query("SELECT * FROM trax_songs;");
        while (table.next())
        {
            TraxDisc Disc = new TraxDisc();
            
            Disc.Id = table.getInt("id");
            Disc.Name = table.getString("name");
            Disc.SongData = table.getString("data");
            Disc.Length = table.getInt("length");
            Disc.Author = table.getString("author");
            
            Discs.put(Disc.Id, Disc);
        }
        
        table.close();
        DB.Close();
    }
}
