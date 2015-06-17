package threadlocal;

/**
 * Created by vchhieng on 17/06/2015.
 */
public class ThreadExtension extends Thread {
    private final ThreadLocalUtil threadLocalUtil;

    public ThreadExtension(Runnable target, ThreadLocalUtil threadLocalUtil) {
        super(target);
        this.threadLocalUtil = threadLocalUtil;
        this.threadLocalUtil.threadExtensionFinalizedCounter++;
    }
    protected void finalize() throws Throwable {
        System.out.println("ThreadExtension.finalize" + threadLocalUtil.threadExtensionFinalizedCounter);
        threadLocalUtil.setThreadExtensionFinalized();
        super.finalize();
    }
}
