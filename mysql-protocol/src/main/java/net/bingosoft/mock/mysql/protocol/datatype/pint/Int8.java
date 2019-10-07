package net.bingosoft.mock.mysql.protocol.datatype.pint;

/**
 * @author kael.
 */
public class Int8 extends AbstractInt{

    protected Int8(int value) {
        super(value);
    }
    @Override
    public Int8 clone() {
        return PInt.int8(this.getValue());
    }
}
