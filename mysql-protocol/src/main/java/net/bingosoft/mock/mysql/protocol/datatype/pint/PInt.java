package net.bingosoft.mock.mysql.protocol.datatype.pint;

import net.bingosoft.mock.mysql.protocol.ByteArrayAble;
import net.bingosoft.mock.mysql.protocol.support.ByteArray;
import net.bingosoft.mock.mysql.protocol.utils.HexUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * protocol int
 *
 * @author kael.
 * 
 * @see <a href="https://dev.mysql.com/doc/internals/en/integer.html">Integer Types</a>
 */
public interface PInt extends ByteArrayAble {

    int getValue();
    
    default boolean valueEquals(PInt pi){
        if (null == pi){
            return false;
        }
        return this.getValue() == pi.getValue();
    }
    
    static Int1 int1(int value) {
        return new Int1(value);
    }

    static Int2 int2(int value) {
        return new Int2(value);
    }

    static Int3 int3(int value) {
        return new Int3(value);
    }

    static Int4 int4(int value) {
        return new Int4(value);
    }

    static Int6 int6(int value) {
        return new Int6(value);
    }

    static Int8 int8(int value) {
        return new Int8(value);
    }

    static IntLenenc lenenc(int value) {
        return new IntLenenc(value);
    }

    static Int1 readInt1(InputStream is) throws IOException {
        return int1(readInt(1, is));
    }

    static Int2 readInt2(InputStream is) throws IOException {
        return int2(readInt(2, is));
    }

    static Int3 readInt3(InputStream is) throws IOException {
        return int3(readInt(3, is));
    }

    static Int4 readInt4(InputStream is) throws IOException {
        return int4(readInt(4, is));
    }

    static Int6 readInt6(InputStream is) throws IOException {
        return int6(readInt(6, is));
    }

    static Int8 readInt8(InputStream is) throws IOException {
        return int8(readInt(8, is));
    }

    static IntLenenc readLenenc(InputStream is) throws IOException {
        return lenenc(readLenencValue(is));
    }

    static int readLenencValue(InputStream is) throws IOException {
        int i = is.read();
        if (i < 0xfb) {
            return i;
        }
        int test = i & 0xff;
        if (test == 0xfc) {
            return readInt2(is).getValue();
        } else if (test == 0xfd) {
            return readInt3(is).getValue();
        } else if (test == 0xfe) {
            return readInt8(is).getValue();
        }
        throw new IllegalStateException("expect 0xfc or 0xfd or 0xfd but real " + HexUtil.bytes2HexString(new byte[]{(byte) i}));
    }

    static int readInt(int length, InputStream is) throws IOException {
        byte[] bytes = new byte[length];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) is.read();
        }
        return decodeInt(bytes);
    }

    static byte[] encodeLenencInt(int value) {
        if (value < 0xfb) {
            return new byte[]{(byte) value};
        } else if (value < (0x01 << 16)) {
            return ByteArray.create()
                    .concat((byte) 0xfc)
                    .concat(int2(value).toByteArray())
                    .toArray();
        } else if (value < (0x01 << 24)) {
            return ByteArray.create()
                    .concat((byte) 0xfd)
                    .concat(int3(value).toByteArray())
                    .toArray();
        } else {
            return ByteArray.create()
                    .concat((byte) 0xfe)
                    .concat(int8(value).toByteArray())
                    .toArray();
        }
    }

    static int decodeInt(byte[] bytes) {
        int value = 0;
        for(int i = bytes.length-1; i >= 0; i--) {
            value = (value << (i * 8)) | (bytes[i] & 0xff);
        }
        return value;
    }

    static byte[] encodeInt(int length, int value) {
        byte[] bytes = new byte[length];
        for(int i = 0; i < length; i++) {
            int offset = i * 8;
            bytes[i] = (byte) ((value >>> offset) & 0xff);
        }
        return bytes;
    }
    
}
