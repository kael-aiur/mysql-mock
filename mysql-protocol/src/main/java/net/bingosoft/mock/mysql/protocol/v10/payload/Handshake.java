package net.bingosoft.mock.mysql.protocol.v10.payload;

import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int1;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int2;
import net.bingosoft.mock.mysql.protocol.datatype.pint.Int4;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;
import net.bingosoft.mock.mysql.protocol.support.*;

import java.io.IOException;
import java.io.InputStream;

import static net.bingosoft.mock.mysql.protocol.support.Capability.COMBINE_LOWER;
import static net.bingosoft.mock.mysql.protocol.support.Capability.COMBINE_UPPER;
import static net.bingosoft.mock.mysql.protocol.support.Charset.UTF8_GENERAL_CI;
import static net.bingosoft.mock.mysql.protocol.support.Status.SERVER_STATUS_AUTOCOMMIT;

/**
 * @author kael.
 * 
 * @see <a href="https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::Handshake">Protocol::Handshake</a>
 */
public class Handshake implements Payload {

    protected ProtocolVersion protocolVersion  = ProtocolVersion.V10;
    protected ServerVersion   serverVersion    = ServerVersion.MYSQL_57;
    protected Int4            connectionId     = PInt.int4(0x00000000);
    protected AuthPlugin      authPlugin       = AuthPlugin.V552_M2;
    protected Int1            filler           = PInt.int1(0X00);
    protected Int2            capabilityFlag1  = PInt.int2(COMBINE_LOWER.getValue());
    protected Charset         characterSet     = UTF8_GENERAL_CI;
    protected Status          statusFlags      = SERVER_STATUS_AUTOCOMMIT;
    protected Int2            capabilityFlags2 = PInt.int2(COMBINE_UPPER.getValue());

    @Override
    public byte[] toByteArray() {
        return ByteArray.create()
                .concat(protocolVersion.getValue())
                .concat(serverVersion.getValue())
                .concat(connectionId.toByteArray())
                .concat(authPlugin.getAuthPluginDataPart())
                .concat(filler.toByteArray())
                .concat(capabilityFlag1.toByteArray())
                .concat(characterSet.getValue())
                .concat(statusFlags.getValue())
                .concat(capabilityFlags2.toByteArray())
                .concat(authPlugin.getAuthPluginDataLen())
                .concat(authPlugin.getAuthPluginName())
                .toArray();
    }

    @Override
    public Payload read(InputStream is) throws IOException {
        throw new UnsupportedOperationException("unsupported read from is");
    }

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(ServerVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    public Int4 getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(Int4 connectionId) {
        this.connectionId = connectionId;
    }

    public AuthPlugin getAuthPlugin() {
        return authPlugin;
    }

    public void setAuthPlugin(AuthPlugin authPlugin) {
        this.authPlugin = authPlugin;
    }

    public Int1 getFiller() {
        return filler;
    }

    public void setFiller(Int1 filler) {
        this.filler = filler;
    }

    public Int2 getCapabilityFlag1() {
        return capabilityFlag1;
    }

    public void setCapabilityFlag1(Int2 capabilityFlag1) {
        this.capabilityFlag1 = capabilityFlag1;
    }

    public Charset getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(Charset characterSet) {
        this.characterSet = characterSet;
    }

    public Status getStatusFlags() {
        return statusFlags;
    }

    public void setStatusFlags(Status statusFlags) {
        this.statusFlags = statusFlags;
    }

    public Int2 getCapabilityFlags2() {
        return capabilityFlags2;
    }

    public void setCapabilityFlags2(Int2 capabilityFlags2) {
        this.capabilityFlags2 = capabilityFlags2;
    }

}
