package net.bingosoft.mock.mysql.protocol.support;

import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.v10.payload.CommandPayload;
import net.bingosoft.mock.mysql.protocol.v10.payload.QueryCommand;
import net.bingosoft.mock.mysql.protocol.v10.payload.QuitCommand;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * @author kael.
 */
public enum Command {
    COM_SLEEP(0x00, () -> null),
    COM_QUIT(0x01, QuitCommand::new),
    COM_INIT_DB(0x02, () -> null),
    COM_QUERY(0x03, QueryCommand::new),
    COM_FIELD_LIST(0x04, () -> null),
    COM_CREATE_DB(0x05, () -> null),
    COM_DROP_DB(0x06, () -> null),
    COM_REFRESH(0x07, () -> null),
    COM_SHUTDOWN(0x08, () -> null),
    COM_STATISTICS(0x09, () -> null),
    COM_PROCESS_INFO(0x0a, () -> null),
    COM_CONNECT(0x0b, () -> null),
    COM_PROCESS_KILL(0x0c, () -> null),
    COM_DEBUG(0x0d, () -> null),
    COM_PING(0x0e, () -> null),
    COM_TIME(0x0f, () -> null),
    COM_DELAYED_INSERT(0x10, () -> null),
    COM_CHANGE_USER(0x11, () -> null),
    COM_BINLOG_DUMP(0x12, () -> null),
    COM_TABLE_DUMP(0x13, () -> null),
    COM_CONNECT_OUT(0x14, () -> null),
    COM_REGISTER_SLAVE(0x15, () -> null),
    COM_STMT_PREPARE(0x16, () -> null),
    COM_STMT_EXECUTE(0x17, () -> null),
    COM_STMT_SEND_LONG_DATA(0x18, () -> null),
    COM_STMT_CLOSE(0x19, () -> null),
    COM_STMT_RESET(0x1a, () -> null),
    COM_SET_OPTION(0x1b, () -> null),
    COM_STMT_FETCH(0x1c, () -> null),
    COM_DAEMON(0x1d, () -> null),
    COM_BINLOG_DUMP_GTID(0x1e, () -> null),
    COM_RESET_CONNECTION(0x1f, () -> null);
    
    private byte value;
    private Supplier<CommandPayload> supplier;

    Command(int value, Supplier<CommandPayload> supplier) {
        this.value = (byte) value;
        this.supplier = supplier;
    }
    
    public CommandPayload newPayload(){
        return this.supplier.get();
    } 
    
    public byte[] toByteArray(){
        return new byte[]{value};
    }
    
    public static Command read(InputStream is) throws IOException {
        int type = is.read();
        for(Command c : values()){
            if (c.value == (byte) type){
                return c;
            }
        }
        throw new IllegalStateException("command type " + type + " not found");
    }
}
