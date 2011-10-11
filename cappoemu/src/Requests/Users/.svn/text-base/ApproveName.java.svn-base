package Requests.Users;

import Server.Connection;
import Requests.Handler;
import Server.Server;

/*
 *****************
 * @author capos *
 *****************
*/

public class ApproveName extends Handler
{
    @Override
    public void ParseIn(Connection Main, Server Environment)
    {
        Environment.InitPacket(36, Main.ClientMessage);

        String PetName = Main.DecodeString();
        int len = PetName.length();
        if(len < 3)
        {
            Environment.Append(2, Main.ClientMessage);
            Environment.Append("3", Main.ClientMessage);
        }
        else if(len > 15)
        {
            Environment.Append(1, Main.ClientMessage);
            Environment.Append("15", Main.ClientMessage);
        }
        else if(!ValidPetNameChars(PetName,len))
        {
            Environment.Append(3, Main.ClientMessage);
        }
        else
        {
            Environment.Append(0, Main.ClientMessage);
        }
        Environment.EndPacket(Main.Socket, Main.ClientMessage);
    }

    private boolean ValidPetNameChars(String inputStr,int HardCodedLen)
    {
        for(int i=0;i<HardCodedLen;i++)
        {
            if(i==0)
            {
                if(inputStr.charAt(0) == ' ')
                {
                    return false;
                }
            }
            if((inputStr.charAt(i) < 'a' || inputStr.charAt(i) > 'z') && (inputStr.charAt(i) < '0' || inputStr.charAt(i) > '9'))
            {
                return false;
            }
        }

        return true;
    }
}
