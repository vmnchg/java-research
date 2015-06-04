package benchmark;

/**
 * Created by vchhieng on 22/05/2015.
 */
public class ThreeByteFactory implements ObjectFactory {
    private static class ThreeBytes {
        byte b0, b1, b2;
    }
    public Object makeObject() {
        return new ThreeBytes();
    }
}