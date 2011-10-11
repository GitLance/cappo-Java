package Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class Trade
{
    private Player Owner;
    private Player Guest;
    
    private Server Environment;

    public Trade(Server Env, Player owner, Player guest)
    {
        Owner = owner;
        Guest = guest;
        
        Environment = Env;
    }

    public void SetTrade()
    {
        Owner.Trade = this;
        Guest.Trade = this;
        ServerMessage Message = new ServerMessage();
        Environment.InitPacket(104, Message);
        Environment.Append(Owner.Id, Message);
        Environment.Append(true, Message);
        Environment.Append(Guest.Id, Message);
        Environment.Append(true, Message);
        Environment.EndPacket(Owner.Connection.Socket, Message);
        Environment.EndPacket(Guest.Connection.Socket, Message);
    }

    public void CloseTrade()
    {
        
    }
}
