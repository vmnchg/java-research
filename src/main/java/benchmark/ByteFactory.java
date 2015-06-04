package benchmark;

/**
 * Created by vchhieng on 22/05/2015.
 */

public class ByteFactory implements ObjectFactory {
    public Object makeObject() {
        return new Byte((byte)33);
    }
}
