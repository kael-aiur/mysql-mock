package net.bingosoft.mock.mysql.protocol.v10.payload;

import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.datatype.pint.IntLenenc;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kael.
 */
public class ColumnCount implements Payload {
    
    protected IntLenenc value;

    public ColumnCount(int value) {
        this.value = PInt.lenenc(value);
    }

    @Override
    public byte[] toByteArray() {
        return value.toByteArray();
    }

    @Override
    public Payload read(InputStream is) throws IOException {
        throw new UnsupportedOperationException();
    }
}
