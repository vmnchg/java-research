package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class MyValue {
    private final int value;

    public MyValue(int value) {
        this.value = value;
    }

    protected void finalize() throws Throwable {
        System.out.println("MyValue.finalize " + value);
        ThreadLocalUtil.setMyValueFinalized();
        super.finalize();
    }
}