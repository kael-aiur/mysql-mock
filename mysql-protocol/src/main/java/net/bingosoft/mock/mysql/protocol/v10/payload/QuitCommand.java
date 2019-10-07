package net.bingosoft.mock.mysql.protocol.v10.payload;

import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int1;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;
import net.bingosoft.mock.mysql.protocol.support.Command;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kael.
 * 
 * @see <pre>https://dev.mysql.com/doc/internals/en/com-quit.html</pre>
 */
public class QuitCommand implements CommandPayload {
    
    protected Int1 value;
    
    @Override
    public Command getCommand() {
        return Command.COM_QUIT;
    }

    @Override
    public byte[] toByteArray() {
        return value.toByteArray();
    }

    @Override
    public Payload read(InputStream is) throws IOException {
        PInt.int1(is.read());
        return this;
    }
}
