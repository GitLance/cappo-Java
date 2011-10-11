package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 *****************
 * @author capos *
 *****************
*/

public class DatabaseClient
{
    public PreparedStatement stmt;
    public Connection cn;

    public DatabaseClient(String[] db) throws ClassNotFoundException, SQLException
    {
        Class.forName("org.gjt.mm.mysql.Driver");
        cn = DriverManager.getConnection("jdbc:mysql:///" + db[0], db[1], db[2]);
    }

    public void Close()
    {
        try
        {
            stmt.close();
            cn.close();
            finalize();
        }
        catch(Exception ex){ }
        catch(Throwable ex){ }
    }

    public void Update(String query) throws Exception
    {
        if(stmt != null) stmt.close();
        stmt = cn.prepareStatement(query);
        stmt.executeUpdate();
    }
    
    public ResultSet Query(String query) throws Exception
    {
        if(stmt != null) stmt.close();
        stmt = cn.prepareStatement(query);
        return stmt.executeQuery();
    }

    public void SecureExec(String query, String... Values) throws Exception
    {
        if(stmt != null) stmt.close();
        stmt = cn.prepareStatement(query);

        int Index = 0;
        for(String Value : Values)
        {
           stmt.setString(++Index, Value);
        }

        stmt.execute();
    }
    
    public ResultSet SecureQuery(String query, String... Values) throws Exception
    {
        if(stmt != null) stmt.close();
        stmt = cn.prepareStatement(query);
        
        int Index = 0;
        for(String Value : Values)
        {
           stmt.setString(++Index, Value);
        }

        return stmt.executeQuery();
    }
}
