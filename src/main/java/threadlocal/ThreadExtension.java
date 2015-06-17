package threadlocal;

/**
 * Created by vchhieng on 17/06/2015.
 */
public class ThreadExtension extends Thread {
    public ThreadExtension(Runnable target) {
        super(target);
        ThreadLocalUtil.threadExtensionFinalizedCounter++;
    }
    protected void finalize() throws Throwable {
        System.out.println("ThreadExtension.finalize" + ThreadLocalUtil.threadExtensionFinalizedCounter);
        ThreadLocalUtil.setThreadExtensionFinalized();
        super.finalize();
    }
}
