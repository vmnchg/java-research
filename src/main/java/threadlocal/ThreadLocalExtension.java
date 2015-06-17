package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class ThreadLocalExtension<T> extends ThreadLocal<T> {
    private final ThreadLocalUtil threadLocalUtil;

    int num = 0;

    public ThreadLocalExtension(int num, ThreadLocalUtil threadLocalUtil) {
        this.threadLocalUtil = threadLocalUtil;
        this.threadLocalUtil.threadLocalExtensionFinalizedCounter++;
        this.num = num;
    }

    protected void finalize() throws Throwable {
//        System.out.println("ThreadLocalExtension.finalize " + num);
        threadLocalUtil.setThreadLocalExtensionFinalized();
        super.finalize();
    }
}
