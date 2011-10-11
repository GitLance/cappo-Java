package Requests;

import Server.Connection;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
 */

public abstract class Handler
{
    public static final int YouAreController = 42;
    public static final int YouAreNotController = 43;
    public static final int YouAreOwner = 47;
    public static final int FlatControllerAdded = 510;

    public abstract void ParseIn(Connection Main, Server Environment) throws Exception;
}