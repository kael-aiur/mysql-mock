package net.bingosoft.mock.mysql.protocol;

/**
 * @author kael.
 */
public interface PayloadLength extends ByteArrayAble,InputStreamReadable<PayloadLength>{
    int getLength();
    
}
