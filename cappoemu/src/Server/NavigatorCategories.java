package Server;

import java.sql.ResultSet;

/*
 *****************
 * @author capos *
 *****************
*/

public class NavigatorCategories
{
    private String[] Categories;
    private int count = 0;

    public NavigatorCategories(String[] DATA) throws Exception
    {
        DatabaseClient DB = new DatabaseClient(DATA);
        ResultSet tcount = DB.Query("SELECT COUNT(*) FROM navigator_categories;");
        if(tcount.next())
        {
            count = tcount.getInt(1)+1;
            Categories = new String[count];

            ResultSet table = DB.Query("SELECT * FROM navigator_categories;");

            while (table.next())
            {
                Categories[table.getInt("id")] = table.getString("name");
            }
        }
        DB.Close();
    }

    public String GetCategory(int Id)
    {
        return Categories[Id];
    }

    public int CategoriesCount()
    {
        return count;
    }
}