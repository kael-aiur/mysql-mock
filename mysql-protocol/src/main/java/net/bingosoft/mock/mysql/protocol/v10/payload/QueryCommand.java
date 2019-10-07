package net.bingosoft.mock.mysql.protocol.v10.payload;

import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.support.ByteArray;
import net.bingosoft.mock.mysql.protocol.support.Command;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kael.
 * @see <pre>https://dev.mysql.com/doc/internals/en/com-query.html</pre>
 */
public class QueryCommand implements CommandPayload {
    
    protected byte[] bytes;
    
    @Override
    public byte[] toByteArray() {
        return ByteArray.create().concat(bytes).toArray();
    }

    @Override
    public Payload read(InputStream is) throws IOException {
        ByteArray ba = ByteArray.create();
        int i;
        do {
            if(is.available() <= 0){
                break;
            }
            i = is.read();
            if (i == -1){
                break;
            }
            ba.concat((byte) i);
        }while (true);
        bytes = ba.toArray();
        return this;
    }

    @Override
    public Command getCommand() {
        return Command.COM_QUERY;
    }
}
