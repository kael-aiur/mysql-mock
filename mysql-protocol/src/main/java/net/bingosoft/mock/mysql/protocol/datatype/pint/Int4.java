package net.bingosoft.mock.mysql.protocol.datatype.pint;

/**
 * @author kael.
 */
public class Int4 extends AbstractInt{

    protected Int4(int value) {
        super(value);
    }
    @Override
    public Int4 clone() {
        return PInt.int4(this.getValue());
    }
}
