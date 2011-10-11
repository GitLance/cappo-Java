package Server;

import Server.Room.RoomUser;
import Server.Room.Room;

/*
 *****************
 * @author capos *
 *****************
*/

public class PetBot extends BotAI
{
    private int Type;
    private String[] Speech;
    private String[] SpeechNOENERGY = { "ZzZzzzzz", "*Estoy cansado*", "Cansado *Está cansado*", "ZzZzZZzzzZZz", "zzZzzZzzz", "... Con sueño ..", "ZzZzzZ" };
    private String[] SpeechNOTFOUND = { "*Confundido*", "¿Qué quieres?", "No te entiendo", "¿Qué es eso?", "hmm... rico rico" };
    private String[] SpeechREFUSE = { "*Me niego*", " ... ", "¿Quién te crees que eres?", "¿Qué haces?", "Grrrrr", "*Tengo ganas de jugar*", "¿Por qué?" };
    private int NextThink = 0;

    public PetBot(int type)
    {
        Type = type;

        if (Type == 0 || Type == 3)
        {
            Speech = new String[]{ "woof woof woof", "Auuuu auuuu", "wooooof", "Grrrr", "Sentandose", "*Estornunando*", "lay", "Woof" };
        }
        else if(Type == 1)
        {
            Speech = new String[]{ "miauu", "Hmmmm", "*Estornudando", "Lamer pata", "Sentandose", "Oliendo" };
        }
        else if(Type == 2)
        {
            Speech = new String[]{ "Rrrr....Grrrrrg....", "*Abrir boca*", "Tick tock tick....", "*Tengo mucha hambre*", "Mover cola", "Estoy casnado", "Estornudando" };
        }
        else if(Type == 4)
        {
            Speech = new String[]{ "*Dame un salmón fresco por favor*", "Grrrrrrr", "*Estornudando*", "*Las tripas me tiemblan*.", "Grrrr... grrrr", "Estoy cansado" };
        }
        else if(Type == 5)
        {
            Speech = new String[]{ "Oink Oink..", "*Meando*", "Estornudando", "*Tirandose un pedo*", "Oink!", "*Hacer el cerdo*", "Estoy cansado", "oink" };
        }
        else if(Type == 6)
        {
            Speech = new String[]{ "Agr...", "Grrrrr.... grrrr....", "Grrrrr...rawh!", "snf", "Grrrrrrh...", "snf", "lay", "Grr...", "*rugiendo*", "*rugido*" };
        }
        else if(Type == 7)
        {
            Speech = new String[]{ "Auguruuuh...", "Buff", "Augubuff...", "Buffuu...", "*sueño*", "snf", "lay", "Aff" };
        }
        else if(Type == 9)
        {
            Speech = new String[] {"Auguruuuh...", "Buff", "Augubuff...", "Buffuu...", "*sueño*", "snf", "lay", "Aff", "** Agüita dónde bañarme **"};
        }
        else if(Type == 10)
        {
             Speech = new String[] {"Auguruuuh...", "Pio, pio", "Augubuff...", "Buffuu...", "*sueño*", "snf", "lay", "Aff", "** Piando una dulce canción **"};
        }
        else if(Type == 11)
        {
             Speech = new String[] {"Auguruuuh...", "Croac, Croac", "Augubuff...", "Buffuu...", "*sueño*", "snf", "lay", "Aff", "** Soy una princesa **"};
        }
        else if(Type == 12)
        {
             Speech = new String[] {"Auguruuuh...", "Buff", "Augubuff...", "Aaaahhahgf!!", "*sueño*", "snf", "lay", "Aff", "** Cómo eructe me quemo la garganta **"};
        }
        else
        {
            Speech = new String[]{ "NOTFOUNDSPEECH-"+Type, "NOTFOUNDSPEECH-"+Type };
        }
    }

    @Override
    public void OnSelfEnterRoom(Room Room, RoomUser PetUser)
    {
        PetUser.MoveTo(Room.Environment.GetRandomNumber(0, Room.Model.MapSizeX), Room.Environment.GetRandomNumber(0, Room.Model.MapSizeY), true);
    }

    @Override
    public void OnUserSay(Room Room, RoomUser PetUser, RoomUser User, String Message)
    {
        Message = Message.toLowerCase();

        String PetName = PetUser.PetData.Name.toLowerCase();
        if (Message.equals(PetName))
        {
            PetUser.SetRot(Rotation.Calculate(PetUser.X, PetUser.Y, User.X, User.Y));
            return;
        }

        if (Message.startsWith(PetName + " "))
        {
            if(User.Client.Data.Id != PetUser.PetData.OwnerId)
            {
                return;
            }

            Message = Message.substring(PetName.length()+1);

            if (Room.Environment.GetRandomNumber(1, 8) < 5)
            {
                if (PetUser.PetData.Energy < 10)
                {
                    PetUser.Chat(null, Room, SpeechNOENERGY[Room.Environment.GetRandomNumber(0, SpeechNOENERGY.length - 1)], false);
                    PetUser.SetStatus("lay", Double.toString(PetUser.Z));
                }
                else if(Message.charAt(0) == 's')
                {
                    if(Message.equals("silent"))
                    {
                        PetUser.RemoveStatus();
                        PetUser.PetData.GiveExperience(+8, Room, PetUser);
                        PetUser.PetData.GiveNutrition(-2);
                        PetUser.PetData.GiveEnergy(+5);
                    }
                    else if(Message.equals("stand"))
                    {
                        PetUser.RemoveStatus();
                        PetUser.PetData.GiveExperience(+10, Room, PetUser);
                        PetUser.PetData.GiveNutrition(-1);
                        PetUser.PetData.GiveEnergy(+5);
                    }
                    else if(Message.equals("sit"))
                    {
                        PetUser.SetStatus("sit", Double.toString(PetUser.Z));
                        PetUser.PetData.GiveExperience(+10, Room, PetUser);
                        PetUser.PetData.GiveNutrition(-5);
                        PetUser.PetData.GiveEnergy(-5);
                    }
                    else if(Message.equals("sleep"))
                    {
                        PetUser.Chat(null, Room, "ZzzZZZzzzzZzz", false);
                        PetUser.SetStatus("lay", Double.toString(PetUser.Z));
                        PetUser.PetData.GiveExperience(+5, Room, PetUser);
                        PetUser.PetData.GiveNutrition(10);
                        PetUser.PetData.GiveEnergy(+10);
                    }
                }
                else if(Message.charAt(0) == 'h')
                {
                    if(Message.equals("here"))
                    {
                        PetUser.RemoveStatus();
                        PetUser.PetData.GiveExperience(+10, Room, PetUser);
                        PetUser.PetData.GiveNutrition(-10);
                        PetUser.PetData.GiveEnergy(-10);
                        int NewX = User.X;
                        int NewY = User.Y;
                        if (User.RotBody == 4)
                        {
                            NewY = User.Y + 1;
                        }
                        else if (User.RotBody == 0)
                        {
                            NewY = User.Y - 1;
                        }
                        else if (User.RotBody == 6)
                        {
                            NewX = User.X - 1;
                        }
                        else if (User.RotBody == 2)
                        {
                            NewX = User.X + 1;
                        }
                        else if (User.RotBody == 3)
                        {
                            NewX = User.X + 1;
                            NewY = User.Y + 1;
                        }
                        else if (User.RotBody == 1)
                        {
                            NewX = User.X + 1;
                            NewY = User.Y - 1;
                        }
                        else if (User.RotBody == 7)
                        {
                            NewX = User.X - 1;
                            NewY = User.Y - 1;
                        }
                        else if (User.RotBody == 5)
                        {
                            NewX = User.X - 1;
                            NewY = User.Y + 1;
                        }
                        PetUser.MoveTo(NewX, NewY, true);
                    }
                }
                else if(Message.equals("play dead"))
                {
                    PetUser.SetStatus("ded", Double.toString(PetUser.Z));
                    PetUser.PetData.GiveExperience(+10, Room, PetUser);
                    PetUser.PetData.GiveNutrition(-3);
                    PetUser.PetData.GiveEnergy(-5);
                }
                else if(Message.charAt(0) == 'd')
                {
                    if(Message.equals("dead"))
                    {
                        PetUser.SetStatus("ded", Double.toString(PetUser.Z));
                        PetUser.PetData.GiveExperience(+10, Room, PetUser);
                        PetUser.PetData.GiveNutrition(-3);
                        PetUser.PetData.GiveEnergy(-5);
                    }
                    else if(Message.equals("drink"))
                    {
                        PetUser.SetStatus("drk", Double.toString(PetUser.Z));
                        PetUser.Chat(null, Room, "*Drinks*", false);
                        PetUser.PetData.GiveExperience(+10, Room, PetUser);
                        PetUser.PetData.GiveNutrition(+30);
                        PetUser.PetData.GiveEnergy(+40);
                    }
                }
                else if(Message.equals("move"))
                {
                    PetUser.RemoveStatus();
                    PetUser.MoveTo(Room.Environment.GetRandomNumber(1, Room.Model.MapSizeX), Room.Environment.GetRandomNumber(1, Room.Model.MapSizeY), true);
                    PetUser.PetData.GiveExperience(+10, Room, PetUser);
                    PetUser.PetData.GiveNutrition(-5);
                    PetUser.PetData.GiveEnergy(-10);
                }
                else if(Message.equals("jump"))
                {
                    PetUser.SetStatus("jmp", Double.toString(PetUser.Z));
                    PetUser.PetData.GiveExperience(+15, Room, PetUser);
                    PetUser.PetData.GiveNutrition(-20);
                    PetUser.PetData.GiveEnergy(-20);
                }
                else if(Message.equals("eat"))
                {
                    PetUser.SetStatus("eat", Double.toString(PetUser.Z));
                    PetUser.Chat(null, Room, "*Eats*", false);
                    PetUser.PetData.GiveExperience(+10, Room, PetUser);
                    PetUser.PetData.GiveNutrition(+10);
                    PetUser.PetData.GiveEnergy(+10);
                }
                else if(Message.equals("free"))
                {
                    PetUser.PetData.GiveExperience(+5, Room, PetUser);
                    PetUser.PetData.GiveNutrition(+5);
                    PetUser.PetData.GiveEnergy(-20);
                }
                else
                {
                    PetUser.Chat(null, Room, SpeechNOTFOUND[Room.Environment.GetRandomNumber(0, SpeechNOTFOUND.length - 1)], false);
                    PetUser.PetData.GiveExperience(-8, Room, PetUser);
                    PetUser.PetData.GiveNutrition(PetUser.PetData.Nutrition - 15);
                    PetUser.PetData.GiveEnergy(PetUser.PetData.Energy - 15);
                }

            }
            else
            {
                PetUser.Chat(null, Room, SpeechREFUSE[Room.Environment.GetRandomNumber(0, SpeechREFUSE.length - 1)], false);
                PetUser.RemoveStatus();
                PetUser.PetData.GiveEnergy(-10);
            }
        }
    }

    @Override
    public void OnTimerTick(Room Room, RoomUser PetUser)
    {
        int timenow = Room.Environment.GetTimestamp();
        if(timenow < NextThink) return;

        int r = Room.Environment.GetRandomNumber(1, 35);
        if(r == 1)
        {
            PetUser.MoveTo(Room.Environment.GetRandomNumber(1, Room.Model.MapSizeX), Room.Environment.GetRandomNumber(1, Room.Model.MapSizeY),true);
            NextThink = timenow + 4;
        }
        else if(r == 2)
        {
            String rSpeech = Speech[Room.Environment.GetRandomNumber(0, Speech.length - 1)];
            if (rSpeech.length() != 3)
            {
                PetUser.Chat(null, Room, rSpeech, false);
            }
            else
            {
                PetUser.SetStatus(rSpeech, Double.toString(PetUser.Z));
            }
            NextThink = timenow + 3;
        }
        else if(r == 35)
        {
            PetUser.PetData.GiveEnergy(+10);
        }
    }

    @Override
    public void OnUserLeaveRoom(Room Room, RoomUser PetUser, Connection Client) { }
    public void OnUserEnterRoom(Room Room, RoomUser PetUser, RoomUser User) { }
    public void OnSelfLeaveRoom(Room Room, RoomUser PetUser, boolean Kicked) { }
}
