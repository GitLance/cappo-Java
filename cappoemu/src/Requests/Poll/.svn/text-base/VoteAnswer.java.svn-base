package Requests.Poll;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class VoteAnswer extends Handler
{
    private int[] Results = new int[6];
    private int Total;

    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Results[Main.DecodeInt()-1]++;
        Total++;

        Environment.InitPacket(80, Main.ClientMessage);
        Environment.Append("Que puntaje le das al Emu", Main.ClientMessage);
        Environment.Append(6, Main.ClientMessage);

        Environment.Append(0, Main.ClientMessage);
        Environment.Append("5", Main.ClientMessage);
        Environment.Append(Results[0], Main.ClientMessage);

        Environment.Append(1, Main.ClientMessage);
        Environment.Append("6", Main.ClientMessage);
        Environment.Append(Results[1], Main.ClientMessage);

        Environment.Append(2, Main.ClientMessage);
        Environment.Append("7", Main.ClientMessage);
        Environment.Append(Results[2], Main.ClientMessage);

        Environment.Append(3, Main.ClientMessage);
        Environment.Append("8", Main.ClientMessage);
        Environment.Append(Results[3], Main.ClientMessage);

        Environment.Append(4, Main.ClientMessage);
        Environment.Append("9", Main.ClientMessage);
        Environment.Append(Results[4], Main.ClientMessage);

        Environment.Append(5, Main.ClientMessage);
        Environment.Append("10", Main.ClientMessage);
        Environment.Append(Results[5], Main.ClientMessage);

        Environment.Append(Total, Main.ClientMessage);

        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
