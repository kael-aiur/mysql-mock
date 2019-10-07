package net.bingosoft.mock.mysql.mysql57;

import net.bingosoft.mock.mysql.AuthHandler;
import net.bingosoft.mock.mysql.Authentication;
import net.bingosoft.mock.mysql.protocol.MysqlPacket;
import net.bingosoft.mock.mysql.protocol.utils.HexUtil;
import net.bingosoft.mock.mysql.protocol.v10.MysqlPacketFactory;
import net.bingosoft.mock.mysql.protocol.v10.payload.Handshake;
import net.bingosoft.mock.mysql.protocol.v10.payload.HandshakeResponse41;
import net.bingosoft.mock.mysql.protocol.v10.payload.OK;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.Socket;

/**
 * @author kael.
 */
public class Mysql57AuthHandler implements AuthHandler {
    @Override
    public Authentication authenticate(Socket socket) throws AuthenticationException, IOException {
        MysqlPacket packet = MysqlPacketFactory.withPayload(new Handshake());
        System.out.println(HexUtil.bytes2HexString(packet.toByteArray()));
        socket.getOutputStream().write(packet.toByteArray());
        MysqlPacket<HandshakeResponse41> resp = MysqlPacketFactory.withPayload(new HandshakeResponse41());
        resp.read(socket.getInputStream());
        String username = resp.getPayload().getUsername().toString();
        String database = resp.getPayload().getDatabase().toString();
        if (null == username || username.isEmpty() || null == database || database.isEmpty()){
            throw new AuthenticationException("username or database is empty");
        }
        MysqlPacket ok = MysqlPacketFactory.withPayload(new OK());
        socket.getOutputStream().write(ok.toByteArray());
        return new SimpleAuthentication(username,database);
    }
    
}
