package net.bingosoft.mock.mysql.protocol.support;

/**
 * @author kael.
 */
public enum ProtocolVersion {
    V10((byte) 0x0a);
    
    private final byte v;

    ProtocolVersion(byte v) {
        this.v = v;
    }
    
    public byte getValue(){
        return v;
    }
    
}
