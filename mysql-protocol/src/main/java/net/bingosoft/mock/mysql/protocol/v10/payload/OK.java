package net.bingosoft.mock.mysql.protocol.v10.payload;

import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int1;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int2;
import net.bingosoft.mock.mysql.protocol.datatype.pint.IntLenenc;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.PString;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.StrEOF;
import net.bingosoft.mock.mysql.protocol.support.ByteArray;
import net.bingosoft.mock.mysql.protocol.support.Status;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kael.
 */
public class OK implements Payload {
    protected Int1 header = PInt.int1(0);
    protected IntLenenc affectedRows = PInt.lenenc(0);
    protected IntLenenc lastInsertId = PInt.lenenc(0);
    protected Status    statusFlags = Status.SERVER_STATUS_AUTOCOMMIT;
    protected Int2      warnings = PInt.int2(0);
    protected StrEOF    info = PString.strEOF("ok".getBytes());
    
    @Override
    public byte[] toByteArray() {
        return ByteArray.create()
                .concat(header.toByteArray())
                .concat(affectedRows.toByteArray())
                .concat(lastInsertId.toByteArray())
                .concat(statusFlags.getValue())
                .concat(warnings.toByteArray())
                // .concat(info.toByteArray())
                .toArray();
    }

    @Override
    public Payload read(InputStream is) throws IOException {
        throw new UnsupportedOperationException("unsupported read ok");
    }
}
