package net.bingosoft.mock.mysql.protocol.v10;

import net.bingosoft.mock.mysql.protocol.SequenceId;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int1;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kael.
 */
public class SimpleSequenceId implements SequenceId {
    private Int1 id;

    public SimpleSequenceId(int id) {
        this.id = PInt.int1(id);
    }

    @Override
    public byte[] toByteArray() {
        return id.toByteArray();
    }

    @Override
    public SequenceId read(InputStream is) throws IOException {
        this.id = PInt.readInt1(is);
        return this;
    }
}
