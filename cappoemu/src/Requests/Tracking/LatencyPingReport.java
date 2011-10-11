package Requests.Tracking;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class LatencyPingReport extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        int Promedio1 = Main.DecodeInt(); // Promedio de todos los ping procesados (latencytest.report.index)
        int Promedio2 = Main.DecodeInt(); // Promedio de los ping por debajo de (Promedio1 * 2)
        int CountPings = Main.DecodeInt(); // Cantidad de Ping Procesados
    }
}
