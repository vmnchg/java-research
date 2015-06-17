package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class ThreadLocalUser {
    private final int num;
    private ThreadLocalExtension<MyObject> value;

    public ThreadLocalUser(int num) {
        ThreadLocalUtil.threadLocalUserFinalizedCounter++;
        this.num = num;
        value = new ThreadLocalExtension<MyObject>(num);
    }

    protected void finalize() throws Throwable {
//        System.out.println("ThreadLocalUser.finalize " + num);
        ThreadLocalUtil.setThreadLocalUserFinalized();
        super.finalize();
    }

    public void setThreadLocal(MyObject myObject) {
        value.set(myObject);
    }

    public void remove() {
        value.remove();
    }
}
