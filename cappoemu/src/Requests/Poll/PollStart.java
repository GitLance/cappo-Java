package Requests.Poll;

import Server.Connection;
import Requests.Handler;
import Server.Server;


/*
 *****************
 * @author capos *
 *****************
*/

public class PollStart extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int PollId = Main.DecodeInt();

        if(PollId == 1773)
        {

            Environment.InitPacket(317, Main.ClientMessage);
            Environment.Append(1773, Main.ClientMessage); // Poll Id
            Environment.Append("Queremos tu opinion!", Main.ClientMessage);
            Environment.Append("Gracias por tus comentarios!", Main.ClientMessage);

            Environment.Append(1, Main.ClientMessage); // question count
            Environment.Append(6070, Main.ClientMessage); // question id
            Environment.Append(1, Main.ClientMessage); // question number
            Environment.Append(4, Main.ClientMessage); // question type
            Environment.Append("¿Tiene alguna idea de cómo podría mejorar este emulador? Si es así, por favor escriba aquí. :)\nSe puede pensar, por ejemplo, lo siguiente:\n¿Qué caracteristicas/funciones faltan?\n¿Qué podría mejorarse?", Main.ClientMessage);
            Environment.EndPacket(Main.Socket, Main.ClientMessage);
        }
    }
}
