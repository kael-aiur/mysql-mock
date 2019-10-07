package net.bingosoft.mock.mysql.protocol.datatype.pint;

/**
 * @author kael.
 */
public class Int1 extends AbstractInt {
    
    protected Int1(int value) {
        super(value);
    }

    @Override
    public Int1 clone() {
        return PInt.int1(this.getValue());
    }
}
