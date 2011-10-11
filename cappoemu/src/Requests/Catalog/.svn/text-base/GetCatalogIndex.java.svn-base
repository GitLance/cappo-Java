package Requests.Catalog;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class GetCatalogIndex extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.EndPacket(Main.Socket, Environment.CatalogIndex);
    }
    
}
