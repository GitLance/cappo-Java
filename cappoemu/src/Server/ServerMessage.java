package Server;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;

/*
 *****************
 * @author capos *
 *****************
*/

public class ServerMessage
{
    public boolean Ready;
    public ChannelBuffer bufferout;
    public ChannelBufferOutputStream output;
        
    public ServerMessage()
    {
        bufferout = ChannelBuffers.buffer(20000);
        output = new ChannelBufferOutputStream(bufferout);
    }
            
}