package net.bingosoft.mock.mysql.protocol.support;

import net.bingosoft.mock.mysql.protocol.ByteArrayAble;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;

/**
 * @author kael.
 */
public interface ColumnDecimals extends ByteArrayAble {
    int INTEGER_STATIC_STRING = 0x00;
    int DOUBLE_FLOAT_DYNAMIC_STRING = 0x1f;
    
    ColumnDecimals INTEGER = () -> INTEGER_STATIC_STRING;
    ColumnDecimals STATIC_STRING = () -> INTEGER_STATIC_STRING;
    ColumnDecimals DOUBLE = () -> DOUBLE_FLOAT_DYNAMIC_STRING;
    ColumnDecimals FLOAT = () -> DOUBLE_FLOAT_DYNAMIC_STRING;
    ColumnDecimals DYNAMIC_STRING = () -> DOUBLE_FLOAT_DYNAMIC_STRING;
    
    static ColumnDecimals decimals(int value){
        if (value <= 0x00 || value >= 0x51){
            throw new IllegalArgumentException("value must great than 0x00 and less than 0x51");
        }
        return () -> value;
    }

    @Override
    default byte[] toByteArray() {
        return PInt.int1(getValue()).toByteArray();
    }
    
    int getValue();
}
