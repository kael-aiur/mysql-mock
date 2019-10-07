package net.bingosoft.mock.mysql.protocol.datatype.pint;

/**
 * @author kael.
 */
public class IntLenenc  implements PInt {
    
    protected int value;

    public IntLenenc(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public byte[] toByteArray() {
        return PInt.encodeLenencInt(getValue());
    }

    @Override
    public IntLenenc clone() {
        return PInt.lenenc(this.getValue());
    }
}
