package net.bingosoft.mock.mysql.protocol.datatype.pint;

/**
 * @author kael.
 */
public class Int6 extends AbstractInt{
    
    protected Int6(int value) {
        super(value);
    }
    @Override
    public Int6 clone() {
        return PInt.int6(this.getValue());
    }
}
