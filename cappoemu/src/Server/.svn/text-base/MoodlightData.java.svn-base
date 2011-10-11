package Server;

import java.util.ArrayList;
import java.util.List;

/*
 *****************
 * @author capos *
 *****************
*/

public class MoodlightData
{
    public class MoodlightPreset
    {
        public String ColorCode;
        public int ColorIntensity;
        public boolean BackgroundOnly;

        public MoodlightPreset(String ColorCode, int ColorIntensity, boolean BackgroundOnly)
        {
            this.ColorCode = ColorCode;
            this.ColorIntensity = ColorIntensity;
            this.BackgroundOnly = BackgroundOnly;
        }
    }

    public boolean Enabled;
    public int CurrentPreset;
    public List<MoodlightPreset> Presets;

    public int ItemId;

    public MoodlightData()
    {
        Presets = new ArrayList<MoodlightPreset>();
    }

    public void AddPresent(String Present)
    {
        Presets.add(GeneratePreset(Present));
    }

    public String GenerateExtraData()
    {
        MoodlightPreset Preset = GetPreset(CurrentPreset);
        return (Enabled?"2":"1")+","+CurrentPreset+","+(Preset.BackgroundOnly?"2":"1")+","+Preset.ColorCode+","+Preset.ColorIntensity;
    }

    public void UpdatePreset(String Color, int Intensity, boolean BgOnly)
    {
        if (!IsValidColor(Color) || !IsValidIntensity(Intensity))
        {
            return;
        }

        MoodlightPreset Preset = GetPreset(CurrentPreset);
        Preset.ColorCode = Color;
        Preset.ColorIntensity = Intensity;
        Preset.BackgroundOnly = BgOnly;
    }
    
    private MoodlightPreset GeneratePreset(String Data)
    {
        String[] Bits = Data.split(",");

        if (!IsValidColor(Bits[0]))
        {
            Bits[0] = "#000000";
        }

        return new MoodlightPreset(Bits[0], Integer.parseInt(Bits[1]), Bits[2].equals("1"));
    }

    private MoodlightPreset GetPreset(int i)
    {
        return Presets.get(--i);
    }

    private boolean IsValidIntensity(int Intensity)
    {
        if (Intensity < 0 || Intensity > 255)
        {
            return false;
        }

        return true;
    }

    private boolean IsValidColor(String ColorCode)
    {
        if(ColorCode.equals("#000000"))
        {
            return true;
        }
        if(ColorCode.equals("#0053F7"))
        {
            return true;
        }
        if(ColorCode.equals("#EA4532"))
        {
            return true;
        }
        if(ColorCode.equals("#82F349"))
        {
            return true;
        }
        if(ColorCode.equals("#74F5F5"))
        {
            return true;
        }
        if(ColorCode.equals("#E759DE"))
        {
            return true;
        }
        if(ColorCode.equals("#F2F851"))
        {
            return true;
        }

        return false;
    }
}
