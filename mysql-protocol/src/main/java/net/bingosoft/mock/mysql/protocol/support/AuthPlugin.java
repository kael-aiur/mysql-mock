package net.bingosoft.mock.mysql.protocol.support;

import net.bingosoft.mock.mysql.protocol.datatype.pint.Int1;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;

/**
 * @author kael.
 */
public enum AuthPlugin {
    V552_M2("5.5.2-m2", PInt.int1(0), "5.5.2-m2");

    AuthPlugin(String authPluginDataPart, Int1 authPluginDataLen, String authPluginName) {
        this.authPluginDataPart = authPluginDataPart;
        this.authPluginDataLen = authPluginDataLen;
        this.authPluginName = authPluginName;
    }

    private String authPluginDataPart;
    private Int1   authPluginDataLen;
    private String authPluginName;
    
    public byte[] getAuthPluginDataPart(){
        return ByteArray.create()
                .concat(authPluginDataPart.getBytes())
                .concat(new byte[]{0,0,0,0,0,0,0,0})
                .toArray(0,8);
    }
    
    public byte[] getAuthPluginDataLen(){
        return authPluginDataLen.toByteArray();
    }
    
    public byte[] getAuthPluginName(){
        return ByteArray.create()
                .concat(authPluginName.getBytes())
                .concat((byte) 0x00)
                .toArray();
    }
    
}
