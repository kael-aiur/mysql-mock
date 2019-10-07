package net.bingosoft.mock.mysql.protocol.v10.payload;

import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.support.Command;

/**
 * @author kael.
 */
public interface CommandPayload extends Payload {
    Command getCommand();
}
