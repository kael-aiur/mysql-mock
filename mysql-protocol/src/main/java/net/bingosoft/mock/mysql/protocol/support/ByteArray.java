package net.bingosoft.mock.mysql.protocol.support;

/**
 * @author kael.
 */
public class ByteArray {

    private byte[] bytes = new byte[0];

    public static ByteArray create() {
        return new ByteArray();
    }

    public ByteArray concat(byte ... con) {
        if (null == con || con.length == 0){
            return this;
        }
        byte[] n = new byte[con.length + bytes.length];
        System.arraycopy(bytes, 0, n, 0, bytes.length);
        System.arraycopy(con, 0, n, bytes.length, con.length);
        this.bytes = n;
        return this;
    }

    public byte[] toArray(){
        return bytes;
    }
    
    public byte[] toArray(int from, int to){
        byte[] n = new byte[to - from];
        System.arraycopy(bytes, from, n, 0, n.length);
        return n;
    }
    
}
