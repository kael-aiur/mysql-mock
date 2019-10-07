package net.bingosoft.mock.mysql.protocol.datatype.pstring;

import net.bingosoft.mock.mysql.protocol.support.ByteArray;

/**
 * @author kael.
 */
public class StrFix extends AbstractStr{
    public StrFix(byte[] str) {
        super(str);
    }
    @Override
    public byte[] toByteArray() {
        return ByteArray.create()
                .concat(str)
                .toArray();
    }

    @Override
    public StrFix clone() {
        return PString.strFix(this.toByteArray());
    }
}
