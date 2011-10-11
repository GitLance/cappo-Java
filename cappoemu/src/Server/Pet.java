package Server;

import Server.Room.RoomUser;
import Server.Room.Room;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class Pet
{
    public String Name;
    public String Color;
    public String OwnerName;
    public int Id;
    public int OwnerId;
    public int Type;
    public int Race;
    public int TimeCreated;
    public int Experience;
    public int Energy;
    public int Nutrition;
    public int Respects;
    public int Level;
    public int X;
    public int Y;
    public int Z;
    public int MaxLevel;

    public int[] ExperienceLevels;
    public int[] MaxEnergyLevels;
    public int[] MaxNutritionLevels;

    public Pet(int id, int owner, String ownname, String name, int type, int race, String color, int timestamp, int nutrition, int experience, int energy, int level, int respects)
    {
        Id = id;
        OwnerId = owner;
        OwnerName = ownname;
        Name = name;
        Type = type;
        Race = race;
        Color = color;
        TimeCreated = timestamp;
        Nutrition = nutrition;
        Experience = experience;
        Energy = energy;
        Level = level;
        Respects = respects;

        ExperienceLevels = new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 250, 500, 750, 1000, 1250, 1500, 1750, 2000, 2250, 2500, 5000, 7500, 10000, 12500, 15000, 20000, 30000, 40000, 50000, 75000 , 100000 , 500000 };
        MaxEnergyLevels = new int[ExperienceLevels.length];
        MaxEnergyLevels[0] = 100;
        for(int i = 1;i<MaxEnergyLevels.length;i++)
        {
            MaxEnergyLevels[i] = MaxEnergyLevels[i-1];
        }
        MaxNutritionLevels = new int[ExperienceLevels.length];
        MaxNutritionLevels[0] = 100;
        for(int i = 1;i<MaxNutritionLevels.length;i++)
        {
            MaxNutritionLevels[i] = MaxNutritionLevels[i-1];
        }
        MaxLevel = ExperienceLevels.length;
    }

    public void GiveExperience(int ammount, Room Room, RoomUser PetUser)
    {
        Experience += ammount;

        ServerMessage Message = new ServerMessage();
        Room.Environment.InitPacket(609, Message);
        Room.Environment.Append(Id, Message);
        Room.Environment.Append(PetUser.VirtualId, Message);
        Room.Environment.Append(ammount, Message);
        Room.SendMessage(Message);

        int c_lev = Level;
        while (true)
        {
            if (Level >= MaxLevel)
            {
                break;
            }

            if (Experience > ExperienceLevels[Level])
            {
                Level++;
            }
            else
            {
                if (c_lev < Level)
                {
                    Player Client = Room.Environment.ClientManager.GetClient(OwnerId);

                    if(Client!=null && (Client.Flags & Server.plrOnline) != Server.plrOnline)
                    {
                        // up level
                        Room.Environment.InitPacket(602, Message);
                        Room.Environment.Append(PetUser.VirtualId, Message);
                        Room.Environment.Append(Name, Message);
                        Room.Environment.Append(Level, Message);
                        Room.Environment.Append(Type, Message);
                        Room.Environment.Append(Race, Message);
                        Room.Environment.Append(Color, Message);
                        Room.Environment.EndPacket(Client.Connection.Socket, Message);
                    }
                }
                break;
            }
        }
    }

    public void GiveEnergy(int ammount)
    {
        Energy += ammount;
    }

    public void GiveNutrition(int ammount)
    {
        Nutrition += ammount;
    }

    public void GiveRespect(Room Room)
    {
        ServerMessage Message = new ServerMessage();
        Room.Environment.InitPacket(440, Message);
        Room.Environment.Append(Id, Message);
        Room.Environment.Append(++Respects, Message);
        Room.SendMessage(Message);
    }

    public int GetExperience()
    {
        return Experience;
    }

    public int GetEnergy()
    {
        return Energy;
    }

    public int GetNutrition()
    {
        return Nutrition;
    }

    public void SerializeInventory(Server Environment, ServerMessage Message)
    {
        Environment.Append(Id, Message);
        Environment.Append(Name, Message);
        Environment.Append(Type, Message);
        Environment.Append(Race, Message);
        Environment.Append(Color, Message);
    }
}
