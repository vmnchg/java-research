package threadlocal;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by vmc on 16/06/2015.
 * 164
 */

@RunWith(JUnit4.class)
public class ThreadLocalTest extends TestCase {
    private void collectGarbage() {
        for (int i = 0; i < 10; i++) {
            System.gc();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Before
    public void setup() {
        ThreadLocalUtil.setUp();
    }

    @Test
    public void threadLocalRemoveMethodWillReleaseObjectFromMemory() {
        ThreadLocalUser user = new ThreadLocalUser();
        MyValue value = new MyValue(1);
        user.setThreadLocal(value);
        user.clear(); // vmc:this is where remove is called
        value = null;
        collectGarbage();

        assertTrue(ThreadLocalUtil.myValueFinalized);
        assertFalse(ThreadLocalUtil.threadLocalUserFinalized);
        assertFalse(ThreadLocalUtil.threadLocalExtensionFinalized);
    }

    @Test
    public void nullifyAnObjectContainsThreadLocalWillNotReleaseObjectOnThreadLocalFromMemory() {
        ThreadLocalUser user = new ThreadLocalUser();
        MyValue value = new MyValue(1);
        user.setThreadLocal(value);
        value = null;
        user = null; // this is where the nullify happens
        collectGarbage();

        assertFalse(ThreadLocalUtil.myValueFinalized);
        // did not expect this to be true. sun might do something clever to cascade null.
        assertTrue(ThreadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(ThreadLocalUtil.threadLocalUserFinalized);
    }

    @Test
    public void test4() throws InterruptedException {
        Executor singlePool = Executors.newSingleThreadExecutor();
        singlePool.execute(new Runnable() {
            public void run() {
                ThreadLocalUser user = new ThreadLocalUser();
                MyValue value = new MyValue(1);
                user.setThreadLocal(value);
            }
        });
        Thread.sleep(100);
        collectGarbage();

        assertFalse(ThreadLocalUtil.myValueFinalized);
        assertTrue(ThreadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(ThreadLocalUtil.threadLocalUserFinalized);
    }

    @Test
    public void test5() throws Exception {
        for (int i = 0; i < 100; i++) {
            ThreadLocalUser user = new ThreadLocalUser(i);
            MyValue value = new MyValue(i);
            user.setThreadLocal(value);
            value = null;
            user = null;
        }
        collectGarbage();

        assertFalse(ThreadLocalUtil.myValueFinalized);
        assertTrue(ThreadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(ThreadLocalUtil.threadLocalUserFinalized);
    }

    @Test
    public void shouldClearMyValueAfterThreadLocalUser() throws Exception {
        for (int i = 0; i < 100; i++) {
            ThreadLocalUser user = new ThreadLocalUser(i);
            MyValue value = new MyValue(i);
            user.setThreadLocal(value);
            value = null;
            user = null;
            collectGarbage();
        }

        assertTrue(ThreadLocalUtil.myValueFinalized);
        assertTrue(ThreadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(ThreadLocalUtil.threadLocalUserFinalized);
    }
}

