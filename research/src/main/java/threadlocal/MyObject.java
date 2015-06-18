package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class MyObject {
    private final ThreadLocalUtil threadLocalUtil;

    private final int value;

    public MyObject(int value, ThreadLocalUtil threadLocalUtil) {
        this.value = value;
        this.threadLocalUtil = threadLocalUtil;
        this.threadLocalUtil.myObjectFinalizedCounter++;
    }

    protected void finalize() throws Throwable {
//        System.out.println("MyObject.finalize " + value);
        threadLocalUtil.setMyValueFinalized();
        super.finalize();
    }
}