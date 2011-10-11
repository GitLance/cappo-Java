package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
 *****************
 * @author capos *
 *****************
*/

public class Mus extends Thread
{
    private Server Environment;
    private ServerSocket Mus;

    public Mus(int MusPort, Server Env)
    {
        Environment = Env;
        try
        {
            System.out.println("Opening Mus Port : " + MusPort);
            Mus = new ServerSocket(MusPort);
        } catch (IOException ex) {

        }
    }

    @Override
    public void run()
    {
        DataInputStream in;
        PrintWriter out;
        byte[] msg = new byte[2];

        int len;

        while (!Mus.isClosed())
        {
            try
            {
                Socket Client = null;
                try
                {
                    Client = Mus.accept();

                    try
                    {
                        in = new DataInputStream(Client.getInputStream());
                        out = new PrintWriter(Client.getOutputStream(), true);
                    }
                    catch (Exception ex)
                    {
                        continue;
                    }

                    in.readFully(msg);

                    String MsgId = new String(msg);

                    if(MsgId.equals("00")) // Get New User id
                    {
                        out.write(Integer.toString(Environment.GenerateUserId()));
                        out.flush();
                    }
                    else if(MsgId.equals("01")) // Banner Generator
                    {
                        in.readFully(msg);
                        MsgId = new String(msg);
                        len = 0;
                        try
                        {
                            len = Integer.parseInt(MsgId);
                        }
                        catch(Exception ex){}

                        if(len > 0)
                        {
                            int timestamp = Environment.GetTimestamp();
                            byte[] pmsg = new byte[len];
                            Client.setSoTimeout(500);
                            try
                            {
                                in.readFully(pmsg);
                            }
                            catch(Exception ex){}

                            System.out.println(new String(pmsg));
                            BannerGenerator banner = Environment.BannersRC4.get(new String(pmsg));
                            if(banner != null/* && banner.timestamp > timestamp*/)
                            {
                                System.out.println(banner.prime.toString()+":"+banner.generator.toString());
                                out.write(banner.prime.toString()+":"+banner.generator.toString());
                            }
                            else
                            {
                                out.write("-1");
                            }
                            out.flush();

                            if(Environment.BannersRC4.size() > 10)
                            {
                                for(String Token : Environment.BannersRC4.keySet())
                                {
                                    banner = Environment.BannersRC4.get(Token);

                                    if(timestamp>banner.timestamp)
                                    {
                                        Environment.BannersRC4.remove(Token);
                                    }
                                }
                            }
                        }
                    }
                    else if(MsgId.equals("99")) // Close Server
                    {
                        System.exit(-1);
                        return;
                    }
                }
                catch (Exception ex)
                {
                    Client.close();
                }
            }
            catch (Exception ex) // exception in .close
            {

            }
        }
    }
}
