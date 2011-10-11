package Requests.Recycler;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetRecyclerPrizes extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int CategoryCount = 5;
        int[] Chance = {0,4,40,200,1000};
        int[][] Category = new int[CategoryCount][];
        Category[0] = new int[]{13,13,13,13,13};
        Category[1] = new int[]{13,13,13,13};
        Category[2] = new int[]{13,13,13};
        Category[3] = new int[]{13,13};
        Category[4] = new int[]{13};

        Environment.InitPacket(506, Main.ClientMessage);
        Environment.Append(CategoryCount, Main.ClientMessage); // Category count
        for(int i = 0;i<CategoryCount;i++)
        {
            Environment.Append(i+1, Main.ClientMessage); // Category
            Environment.Append(Chance[i], Main.ClientMessage);

            Environment.Append(Category[i].length, Main.ClientMessage);

            for(int o = 0;o<Category[i].length;o++)
            {
                Environment.Append("s", Main.ClientMessage);
                Environment.Append(Category[i][o], Main.ClientMessage);
            }
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }
}
