package net.bingosoft.mock.mysql.protocol.datatype.pint;

/**
 * @author kael.
 */
public abstract class AbstractInt implements PInt {
    
    protected int value;
    protected int length;

    protected AbstractInt(int value) {
        this.value = value;
        this.length = parseLength();
    }

    protected int parseLength(){
        String name = this.getClass().getName();
        return Integer.parseInt(name.substring(name.length()-1));
    }
    
    @Override
    public int getValue() {
        return value;
    }


    @Override
    public byte[] toByteArray() {
        int length = length();
        int value = getValue();
        return PInt.encodeInt(length, value);
    }
    
    protected int length(){
        return length;
    }

}
