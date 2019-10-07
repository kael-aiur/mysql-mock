package net.bingosoft.mock.mysql.protocol;

/**
 * @author kael.
 */
public interface MysqlPacket<B extends Payload> extends ByteArrayAble,InputStreamReadable<MysqlPacket> {
    
    PayloadLength getPayloadLength();
    SequenceId getSequenceId();
    B getPayload();

    @Override
    default byte[] toByteArray() {
        byte[] pl = getPayloadLength().toByteArray();
        byte[] si = getSequenceId().toByteArray();
        byte[] p = getPayload().toByteArray();
        byte[] bytes = new byte[pl.length+si.length+p.length];
        System.arraycopy(pl, 0, bytes, 0, pl.length);
        System.arraycopy(si, 0, bytes, pl.length, si.length);
        System.arraycopy(p, 0, bytes, pl.length+si.length, p.length);
        return bytes;
    }

}
