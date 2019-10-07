package net.bingosoft.mock.mysql.protocol.datatype.pstring;

import net.bingosoft.mock.mysql.protocol.datatype.pint.IntLenenc;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;
import net.bingosoft.mock.mysql.protocol.support.ByteArray;

/**
 * @author kael.
 */
public class StrLenenc implements PString{
    
    protected IntLenenc length;
    protected StrFix str;

    public StrLenenc(byte[] str) {
        this.str = PString.strFix(str);
        this.length = PInt.lenenc(str.length);
    }

    @Override
    public int getLength() {
        return length.getValue();
    }

    @Override
    public byte[] toByteArray() {
        return ByteArray.create()
                .concat(length.toByteArray())
                .concat(str.toByteArray())
                .toArray();
    }

    @Override
    public StrLenenc clone(){
        return PString.strLenenc(str.toByteArray());
    }
}
