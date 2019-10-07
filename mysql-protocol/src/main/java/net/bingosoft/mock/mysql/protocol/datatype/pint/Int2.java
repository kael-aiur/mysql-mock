package net.bingosoft.mock.mysql.protocol.datatype.pint;

/**
 * @author kael.
 */
public class Int2 extends AbstractInt{
    protected Int2(int value) {
        super(value);
    }

    @Override
    public Int2 clone() {
        return PInt.int2(this.getValue());
    }
}
