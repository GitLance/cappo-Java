package Requests.Poll;

import Server.Connection;
import Server.DatabaseClient;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class PollAnswer extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int PollId = Main.DecodeInt();

        if(PollId == 1773)
        {
            int QuestionId = Main.DecodeInt();

            if(QuestionId == 6070)
            {
                int count = Main.DecodeInt();

                for(int i = 0;i<count;i++)
                {
                    String Response = Main.DecodeString();


                    DatabaseClient DB;
                    try
                    {
                        DB = new DatabaseClient(Environment.DataBase);
                    }
                    catch (Exception ex)
                    {
                        return;
                    }

                    try
                    {
                        DB.SecureExec("INSERT INTO `poll` (`Response`) VALUES(?);",Response);
                    }
                    catch (Exception ex)
                    {
                    }

                    DB.Close();

                    Environment.InitPacket(79, Main.ClientMessage);
                    Environment.Append("Que puntaje le das al Emu", Main.ClientMessage);
                    Environment.Append(6, Main.ClientMessage);

                    Environment.Append(0, Main.ClientMessage);
                    Environment.Append("5", Main.ClientMessage);

                    Environment.Append(1, Main.ClientMessage);
                    Environment.Append("6", Main.ClientMessage);

                    Environment.Append(2, Main.ClientMessage);
                    Environment.Append("7", Main.ClientMessage);

                    Environment.Append(3, Main.ClientMessage);
                    Environment.Append("8", Main.ClientMessage);

                    Environment.Append(4, Main.ClientMessage);
                    Environment.Append("9", Main.ClientMessage);

                    Environment.Append(5, Main.ClientMessage);
                    Environment.Append("10", Main.ClientMessage);
                    
                    Environment.EndPacket(Main.Socket, Main.ClientMessage);
                }
            }
        }
    }
}
