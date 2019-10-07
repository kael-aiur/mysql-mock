package net.bingosoft.mock.mysql.protocol.datatype.pstring;

import net.bingosoft.mock.mysql.protocol.support.ByteArray;

/**
 * @author kael.
 */
public class StrEOF extends AbstractStr {
    public StrEOF(byte[] str) {
        super(str);
    }

    @Override
    public int getLength() {
        return str.length;
    }

    @Override
    public byte[] toByteArray() {
        return ByteArray.create()
                .concat(str)
                .toArray();
    }
}
