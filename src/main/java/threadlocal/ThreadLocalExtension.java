package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class ThreadLocalExtension<T> extends ThreadLocal<T> {
    int num = 0;
    public ThreadLocalExtension() {
    }

    public ThreadLocalExtension(int num) {
        ThreadLocalUtil.threadLocalExtensionFinalizedCounter++;
        this.num = num;
    }

    protected void finalize() throws Throwable {
//        System.out.println("ThreadLocalExtension.finalize " + num);
        ThreadLocalUtil.setMyThreadLocalFinalized();
        super.finalize();
    }
}
