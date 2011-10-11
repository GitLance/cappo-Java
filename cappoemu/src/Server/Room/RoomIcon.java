package Server.Room;

/*
 *****************
 * @author capos *
 *****************
*/

public class RoomIcon
{
    public int BackgroundImage;
    public int ForegroundImage;
    public String[] Items;

    public RoomIcon(int BackgroundImage, int ForegroundImage, String[] Items)
    {
        this.BackgroundImage = BackgroundImage;
        this.ForegroundImage = ForegroundImage;
        this.Items = Items;
    }
}
