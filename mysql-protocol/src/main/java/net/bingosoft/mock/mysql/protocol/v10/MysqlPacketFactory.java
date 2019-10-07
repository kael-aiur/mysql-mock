package net.bingosoft.mock.mysql.protocol.v10;

import net.bingosoft.mock.mysql.protocol.MysqlPacket;
import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.PayloadLength;
import net.bingosoft.mock.mysql.protocol.SequenceId;
import net.bingosoft.mock.mysql.protocol.support.Command;
import net.bingosoft.mock.mysql.protocol.v10.payload.CommandPayload;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author kael.
 */
public class MysqlPacketFactory {
    public static <B extends Payload> MysqlPacket<B> withPayload(B payload){
        return new DefaultMysqlPacket<>(payload);
    }

    public static MysqlPacket<CommandPayload> readMysqlPacket(InputStream is) throws IOException {
        PayloadLength pl = new SimplePayloadLength(0);
        SequenceId si = new SimpleSequenceId(0);
        pl.read(is);
        si.read(is);
        Command command = Command.read(is);
        CommandPayload payload = command.newPayload();
        payload.read(is);
        return new DefaultMysqlPacket<>(pl, si, payload);
    }
    
}
