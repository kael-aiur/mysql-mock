package net.bingosoft.mock.mysql.protocol.v10;

import net.bingosoft.mock.mysql.protocol.MysqlPacket;
import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.PayloadLength;
import net.bingosoft.mock.mysql.protocol.SequenceId;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kael.
 */
public class DefaultMysqlPacket<B extends Payload> implements MysqlPacket<B> {

    protected PayloadLength length;
    protected SequenceId    sequenceId;
    protected B             payload;
    
    protected DefaultMysqlPacket(B payload) {
        this(payload,(byte)0);
    }
    
    protected DefaultMysqlPacket(B payload, byte sequenceId) {
        this.payload = payload;
        this.sequenceId = new SimpleSequenceId(sequenceId);
    }

    public DefaultMysqlPacket(PayloadLength length, SequenceId sequenceId, B payload) {
        this.length = length;
        this.sequenceId = sequenceId;
        this.payload = payload;
    }

    @Override
    public PayloadLength getPayloadLength() {
        if (null == length){
            this.length = new SimplePayloadLength(this.getPayload().toByteArray().length);
        }
        return length;
    }

    @Override
    public SequenceId getSequenceId() {
        return sequenceId;
    }

    @Override
    public B getPayload() {
        return payload;
    }

    @Override
    public MysqlPacket read(InputStream is) throws IOException {
        getPayloadLength().read(is);
        getSequenceId().read(is);
        getPayload().read(is);
        return this;
    }
}
