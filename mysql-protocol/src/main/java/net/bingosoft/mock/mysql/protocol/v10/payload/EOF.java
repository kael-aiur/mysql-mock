package net.bingosoft.mock.mysql.protocol.v10.payload;

import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int1;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int2;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;
import net.bingosoft.mock.mysql.protocol.support.ByteArray;
import net.bingosoft.mock.mysql.protocol.support.Status;

import java.io.IOException;
import java.io.InputStream;

import static net.bingosoft.mock.mysql.protocol.support.Status.SERVER_STATUS_AUTOCOMMIT;

/**
 * @author kael.
 */
public class EOF implements Payload {
    
    protected Int1   header      = PInt.int1(0xfe);
    protected Int2   warnings    = PInt.int2(0x00);
    protected Status statusFlags = SERVER_STATUS_AUTOCOMMIT;
    
    @Override
    public byte[] toByteArray() {
        return ByteArray.create()
                .concat(header.toByteArray())
                .concat(warnings.toByteArray())
                .concat(statusFlags.getValue())
                .toArray();
    }

    @Override
    public Payload read(InputStream is) throws IOException {
        throw new UnsupportedOperationException();
    }
}
