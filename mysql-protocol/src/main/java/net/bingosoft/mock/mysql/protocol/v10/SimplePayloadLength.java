package net.bingosoft.mock.mysql.protocol.v10;

import net.bingosoft.mock.mysql.protocol.PayloadLength;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int3;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kael.
 */
public class SimplePayloadLength implements PayloadLength {
    
    private Int3 length;

    public SimplePayloadLength(int length) {
        this.length = PInt.int3(length);
    }

    @Override
    public int getLength() {
        return length.getValue();
    }

    @Override
    public byte[] toByteArray() {
        return length.toByteArray();
    }

    @Override
    public PayloadLength read(InputStream is) throws IOException {
        length = PInt.readInt3(is);
        return this;
    }
}
