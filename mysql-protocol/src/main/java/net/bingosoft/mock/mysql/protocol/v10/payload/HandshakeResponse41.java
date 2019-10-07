package net.bingosoft.mock.mysql.protocol.v10.payload;

import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int1;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int4;
import net.bingosoft.mock.mysql.protocol.datatype.pint.IntLenenc;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.PString;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.StrFix;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.StrLenenc;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.StrNul;
import net.bingosoft.mock.mysql.protocol.support.ByteArray;
import net.bingosoft.mock.mysql.protocol.support.Charset;

import java.io.IOException;
import java.io.InputStream;
import java.util.IdentityHashMap;
import java.util.Map;

import static net.bingosoft.mock.mysql.protocol.support.Capability.*;

/**
 * @author kael.
 * 
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::HandshakeResponse41">Protocol::HandshakeResponse41</a>
 */
public class HandshakeResponse41 implements Payload {
    protected Int4    capabilityFlag = PInt.int4(0);
    protected Int4    maxPacketSize = PInt.int4(0);
    protected Charset characterSet = Charset.UTF8_GENERAL_CI;
    protected StrFix  reserved = PString.strFix(new byte[23]);
    protected StrNul  username = PString.strNul(new byte[]{0x00});
    protected PString authResponse = PString.strNul(new byte[]{0x00});
    protected StrNul  database = PString.strNul(new byte[]{0x00});
    protected StrNul  authPluginName = PString.strNul(new byte[]{0x00});
    protected IntLenenc kvLength = PInt.lenenc(0);
    protected Map<StrLenenc, StrLenenc> kv = new IdentityHashMap<>();
    
    @Override
    public byte[] toByteArray() {
        ByteArray array = ByteArray.create()
                .concat(capabilityFlag.toByteArray())
                .concat(maxPacketSize.toByteArray())
                .concat(characterSet.getValue())
                .concat(reserved.toByteArray())
                .concat(username.toByteArray())
                .concat(authResponse.toByteArray())
                .concat(database.toByteArray())
                .concat(authPluginName.toByteArray())
                .concat(kvLength.toByteArray());
        kv.forEach((k, v) -> array.concat(k.toByteArray())
                .concat(v.toByteArray()));
        return array.toArray();
    }

    @Override
    public Payload read(InputStream is) throws IOException {
        capabilityFlag = PInt.readInt4(is);
        maxPacketSize = PInt.readInt4(is);
        characterSet = Charset.read(is);
        reserved = PString.readFix(23, is);
        username = PString.readNul(is);
        authResponse = PString.readLenenc(is);
        /*
        if (CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA.isSupported(capabilityFlag)){
            authResponse = PString.readLenenc(is);
        }else if (CLIENT_SECURE_CONNECTION.isSupported(capabilityFlag)){
            Int1 len = PInt.readInt1(is);
            StrFix str = PString.readFix(len.getValue(), is);
            authResponse = PString.strLenenc(str.toByteArray());
        }else {
            authResponse = PString.readNul(is);
        }
        */
        if (CLIENT_CONNECT_WITH_DB.isSupported(capabilityFlag)){
            database = PString.readNul(is);
        }
        if (CLIENT_PLUGIN_AUTH.isSupported(capabilityFlag)){
            authPluginName = PString.readNul(is);
        }
        if (CLIENT_CONNECT_ATTRS.isSupported(capabilityFlag)){
            IntLenenc len = PInt.readLenenc(is);
            for(int p = 0; p < len.getValue();){
                StrLenenc k = PString.readLenenc(is);
                StrLenenc v = PString.readLenenc(is);
                p = k.getLength() + v.getLength();
                addKeyValue(k,v);
            }
        }
        return this;
    }

    public Int4 getCapabilityFlag() {
        return capabilityFlag;
    }

    public void setCapabilityFlag(Int4 capabilityFlag) {
        this.capabilityFlag = capabilityFlag;
    }

    public Int4 getMaxPacketSize() {
        return maxPacketSize;
    }

    public void setMaxPacketSize(Int4 maxPacketSize) {
        this.maxPacketSize = maxPacketSize;
    }

    public Charset getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(Charset characterSet) {
        this.characterSet = characterSet;
    }

    public StrFix getReserved() {
        return reserved;
    }

    public void setReserved(StrFix reserved) {
        this.reserved = reserved;
    }

    public StrNul getUsername() {
        return username;
    }

    public void setUsername(StrNul username) {
        this.username = username;
    }

    public PString getAuthResponse() {
        return authResponse;
    }

    public void setAuthResponse(PString authResponse) {
        this.authResponse = authResponse;
    }

    public StrNul getDatabase() {
        return database;
    }

    public void setDatabase(StrNul database) {
        this.database = database;
    }

    public StrNul getAuthPluginName() {
        return authPluginName;
    }

    public void setAuthPluginName(StrNul authPluginName) {
        this.authPluginName = authPluginName;
    }

    public IntLenenc getKvLength() {
        return kvLength;
    }

    public void setKvLength(IntLenenc kvLength) {
        this.kvLength = kvLength;
    }

    public Map<StrLenenc, StrLenenc> getKv() {
        return kv;
    }

    public void addKeyValue(StrLenenc key, StrLenenc value) {
        this.kv.put(key,value);
    }
}
