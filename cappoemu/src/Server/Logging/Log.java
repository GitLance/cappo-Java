package Server.Logging;

import java.io.FileWriter;
import java.io.PrintWriter;

/*
 *****************
 * @author capos *
 *****************
*/

public class Log
{
    public PrintWriter out;
    private boolean Developer;

    public Log(boolean Developer, String Date)
    {
        this.Developer = Developer;

        if(!Developer)
        {
            try
            {
                out = new PrintWriter(new FileWriter("log-"+Date.replace(":", "-")+".txt", true));
            }
            catch (Exception ex)
            {
                ex.printStackTrace(System.err);
            }
        }
    }

    public void Print(Exception ex)
    {
        if(Developer)
        {
            ex.printStackTrace();
        }
        else
        {
            ex.printStackTrace(out);
        }
    }

    public void Print(Throwable ex)
    {
        if(Developer)
        {
            ex.printStackTrace();
        }
        else
        {
            ex.printStackTrace(out);
        }
    }

}
