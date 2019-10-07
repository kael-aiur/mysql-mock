package net.bingosoft.mock.mysql.protocol.datatype.pint;

/**
 * @author kael.
 */
public class Int3 extends AbstractInt{
    protected Int3(int value) {
        super(value);
    }
    @Override
    public Int3 clone() {
        return PInt.int3(this.getValue());
    }
}
