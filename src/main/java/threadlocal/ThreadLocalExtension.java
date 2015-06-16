package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class ThreadLocalExtension<T> extends ThreadLocal<T> {
    protected void finalize() throws Throwable {
        System.out.println("ThreadLocalExtension.finalize");
        ThreadLocalUtil.setMyThreadLocalFinalized();
        super.finalize();
    }
}
