package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class MyObject {
    private final int value;

    public MyObject(int value) {
        this.value = value;
        ThreadLocalUtil.myValueFinalizedCounter++;
    }

    protected void finalize() throws Throwable {
//        System.out.println("MyObject.finalize " + value);
        ThreadLocalUtil.setMyValueFinalized();
        super.finalize();
    }
}