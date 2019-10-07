package net.bingosoft.mock.mysql.protocol.datatype.pstring;

/**
 * @author kael.
 */
public abstract class AbstractStr implements PString {
    
    protected byte[] str;

    public AbstractStr(byte[] str) {
        this.str = str;
    }

    @Override
    public int getLength() {
        return str.length;
    }

    @Override
    public String toString() {
        return new String(toByteArray());
    }
}
