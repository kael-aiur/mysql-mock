package net.bingosoft.mock.mysql.protocol.v10.payload;

import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.StrLenenc;
import net.bingosoft.mock.mysql.protocol.support.ByteArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author kael.
 */
public class ResultsetRow implements Payload {
    
    protected List<StrLenenc> columns;

    public ResultsetRow(List<StrLenenc> columns) {
        this.columns = columns;
    }

    @Override
    public byte[] toByteArray() {
        ByteArray array = ByteArray.create();
        columns.forEach(strLenenc -> array.concat(strLenenc.toByteArray()));
        return array.toArray();
    }

    @Override
    public Payload read(InputStream is) throws IOException {
        throw new UnsupportedOperationException();
    }
}
