package net.bingosoft.mock.mysql;

import net.bingosoft.mock.mysql.protocol.MysqlPacket;
import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.v10.payload.CommandPayload;

import java.io.IOException;
import java.net.Socket;

/**
 * @author kael.
 */
public interface CommandHandler {
    
    boolean handle(Socket socket, MysqlPacket<CommandPayload> mp, Authentication authentication) throws IOException;
    
}
