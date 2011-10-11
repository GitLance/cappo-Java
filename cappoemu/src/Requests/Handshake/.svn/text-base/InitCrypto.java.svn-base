package Requests.Handshake;

import Server.Connection;
import Requests.Handler;
import Server.BannerGenerator;
import Server.Crypto.Crypto;
import Server.Server;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

/*
 *****************
 * @author capos *
 *****************
*/

public class InitCrypto extends Handler
{
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder(50);
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    @Override
    public void ParseIn(Connection Main, Server Environment) throws Exception
    {
        Main.Crypto = new Crypto();
        
        BannerGenerator banner = new BannerGenerator();
        banner.timestamp = Environment.GetTimestamp()+3;
        String timestring = Integer.toString(banner.timestamp);

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(timestring.getBytes("iso-8859-1"), 0, timestring.length());
        String Token = convertToHex(messageDigest.digest());

        SecureRandom random = new SecureRandom();
        banner.prime = BigInteger.probablePrime(200, random);
        banner.generator = (new BigInteger("992211000000000", 10)).add(new BigInteger(timestring,10));

        Environment.BannersRC4.put(Token,banner);

        Environment.InitPacket(277, Main.ClientMessage);
        Environment.Append(Token, Main.ClientMessage); // token
        Environment.Append(false, Main.ClientMessage); // not used
        Environment.EndPacket(Main.Socket, Main.ClientMessage);

        Main.Crypto.InitDH(banner.prime, banner.generator, Main.Crypto.generateRandomHexString(30));
    }
}