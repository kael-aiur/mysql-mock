package net.bingosoft.mock.mysql.protocol.support;

/**
 * @author kael.
 */
public enum  ServerVersion {
    MYSQL_57("5.6.23");
    
    private final String server;

    ServerVersion(String server) {
        this.server = server;
    }
    
    public byte[] getValue(){
        return ByteArray.create()
                .concat(this.server.getBytes())
                .concat((byte) 0x00).toArray();
    }
}
