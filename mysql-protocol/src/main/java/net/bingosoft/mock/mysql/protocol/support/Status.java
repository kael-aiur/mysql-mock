package net.bingosoft.mock.mysql.protocol.support;

import net.bingosoft.mock.mysql.protocol.datatype.pint.Int2;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;

/**
 * @author kael.
 */
public enum Status {
    SERVER_STATUS_IN_TRANS(PInt.int2(0x0001)),
    SERVER_STATUS_AUTOCOMMIT(PInt.int2(0x0002)),
    SERVER_MORE_RESULTS_EXISTS(PInt.int2(0x0008)),
    SERVER_STATUS_NO_GOOD_INDEX_USED(PInt.int2(0x0010)),
    SERVER_STATUS_NO_INDEX_USED(PInt.int2(0x0020)),
    SERVER_STATUS_CURSOR_EXISTS(PInt.int2(0x0040)),
    SERVER_STATUS_LAST_ROW_SENT(PInt.int2(0x0080)),
    SERVER_STATUS_DB_DROPPED(PInt.int2(0x0100)),
    SERVER_STATUS_NO_BACKSLASH_ESCAPES(PInt.int2(0x0200)),
    SERVER_STATUS_METADATA_CHANGED(PInt.int2(0x0400)),
    SERVER_QUERY_WAS_SLOW(PInt.int2(0x0800)),
    SERVER_PS_OUT_PARAMS(PInt.int2(0x1000)),
    SERVER_STATUS_IN_TRANS_READONLY(PInt.int2(0x2000)),
    SERVER_SESSION_STATE_CHANGED(PInt.int2(0x4000));
    
    private Int2 value;

    Status(Int2 value) {
        this.value = value;
    }

    public byte[] getValue() {
        return value.toByteArray();
    }
}
