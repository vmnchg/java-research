package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class ThreadLocalUser {
    private final int num;
    private ThreadLocalExtension<MyValue> value =
            new ThreadLocalExtension<MyValue>();

    public ThreadLocalUser() {
        this(0);
    }

    public ThreadLocalUser(int num) {
        this.num = num;
    }

    protected void finalize() throws Throwable {
        System.out.println("ThreadLocalUser.finalize " + num);
        ThreadLocalUtil.setThreadLocalUserFinalized();
        super.finalize();
    }

    public void setThreadLocal(MyValue myValue) {
        value.set(myValue);
    }

    public void clear() {
        value.remove();
    }
}
