package net.bingosoft.mock.mysql.protocol.datatype.pstring;

import net.bingosoft.mock.mysql.protocol.ByteArrayAble;
import net.bingosoft.mock.mysql.protocol.datatype.pint.IntLenenc;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;
import net.bingosoft.mock.mysql.protocol.support.ByteArray;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kael.
 * 
 * @see <a href="https://dev.mysql.com/doc/internals/en/string.html">String Types</a>
 */
public interface PString extends ByteArrayAble {
    int getLength();
    static StrFix strFix(byte[] bytes){
        return new StrFix(bytes);
    }
    static StrNul strNul(byte[] bytes){
        return new StrNul(bytes);
    }
    static StrEOF strEOF(byte[] bytes){
        return new StrEOF(bytes);
    }
    
    static StrLenenc strLenenc(byte[] bytes){
        return new StrLenenc(bytes);
    }
    
    static StrFix readFix(int len,InputStream is) throws IOException{
        byte[] bytes = new byte[len];
        for(int i = 0; i < bytes.length; i ++){
            bytes[i] = (byte) is.read();
        }
        return strFix(bytes);
    }
    
    static StrNul readNul(InputStream is) throws IOException{
        ByteArray array = ByteArray.create();
        do {
            int i = is.read();
            if ((i & 0xff) == 0x00){
                break;
            }
            array.concat((byte) i);
        }while (true);
        return strNul(array.toArray());
    }
    
    static StrLenenc readLenenc(InputStream is) throws IOException {
        IntLenenc len = PInt.readLenenc(is);
        StrFix fix = readFix(len.getValue(), is);
        return strLenenc(fix.toByteArray());
    }
}
