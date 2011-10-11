package Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class AvatarEffect
{
    public int EffectId;
    public int TotalDuration;
    public boolean Activated;
    public int StampActivated;

    public AvatarEffect(int EffectId, int TotalDuration, boolean Activated, int ActivateTimestamp)
    {
        this.EffectId = EffectId;
        this.TotalDuration = TotalDuration;
        this.Activated = Activated;
        this.StampActivated = ActivateTimestamp;
    }
}
