import Server.Server;
import java.util.HashMap;
import java.util.Map;

/*
 *****************
 * @author capos *
 *****************
*/

public class Cappo
{
    public static void main(String[] args)
    {
        Server Server = null;

        try
        {
            String Config = ".\\config.ini";
            if(args.length == 1)
            {
                Config = args[0];
            }
            Server = new Server(Config);
        }
        catch (Exception ex)
        {
            System.err.println(ex.fillInStackTrace());
        }

        try
        {
            Server.Log.out.close();
        }
        catch (Exception ex)
        {  }
    }
}
