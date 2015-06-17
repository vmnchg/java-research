package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class ThreadLocalUser {
    private final ThreadLocalUtil threadLocalUtil;

    private final int num;
    private ThreadLocalExtension<MyObject> value;

    public ThreadLocalUser(int num, ThreadLocalUtil threadLocalUtil) {
        this.threadLocalUtil = threadLocalUtil;
        this.threadLocalUtil.threadLocalUserFinalizedCounter++;
        this.num = num;
        value = new ThreadLocalExtension<MyObject>(num, this.threadLocalUtil);
    }

    protected void finalize() throws Throwable {
//        System.out.println("ThreadLocalUser.finalize " + num);
        threadLocalUtil.setThreadLocalUserFinalized();
        super.finalize();
    }

    public void setThreadLocal(MyObject myObject) {
        value.set(myObject);
    }

    public void remove() {
        value.remove();
    }
}
