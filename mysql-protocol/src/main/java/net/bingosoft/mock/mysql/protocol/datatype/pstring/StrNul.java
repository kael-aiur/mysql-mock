package net.bingosoft.mock.mysql.protocol.datatype.pstring;

import net.bingosoft.mock.mysql.protocol.support.ByteArray;

/**
 * @author kael.
 */
public class StrNul extends AbstractStr {
    public StrNul(byte[] str) {
        super(str);
    }

    @Override
    public byte[] toByteArray() {
        return ByteArray.create()
                .concat(str)
                .concat((byte) 0x00)
                .toArray();
    }
}
