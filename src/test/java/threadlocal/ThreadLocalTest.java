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
    private static final int MEGABYTE = 1024 * 1024;

    private void gc() {
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

    private void increaseMemoryThenGC() {
        final int TOTAL_MEGA_BYTE = 32;
        final int memory = MEGABYTE * TOTAL_MEGA_BYTE;
        byte[] bytes = new byte[memory];

        for (int i = 0 ; i<memory; i++) {
            bytes[i] = (byte) i;
        }
        gc();
    }


    @Before
    public void setup() {
        ThreadLocalUtil.setUp();
    }

//    @Test
    public void threadLocalRemoveMethodWillReleaseObjectFromMemory() {
        ThreadLocalUser user = new ThreadLocalUser(1);
        MyObject value = new MyObject(1);
        user.setThreadLocal(value);
        user.remove(); // vmc:this is where remove is called
        value = null;
        increaseMemoryThenGC();

        assertTrue(ThreadLocalUtil.myObjectFinalized);
        assertFalse(ThreadLocalUtil.threadLocalUserFinalized);
        assertFalse(ThreadLocalUtil.threadLocalExtensionFinalized);

        increaseMemoryThenGC();
        ThreadLocalUtil.printThreadLocalExtensionFinalized();
        ThreadLocalUtil.printThreadLocalUserFinalized();

    }

//    @Test
    public void nullifyAnObjectContainsThreadLocalWillNotReleaseObjectOnThreadLocalFromMemory() {
        ThreadLocalUser user = new ThreadLocalUser(1);
        MyObject value = new MyObject(1);
        user.setThreadLocal(value);
        value = null;
        user = null; // this is where the nullify happens
        increaseMemoryThenGC();

        assertFalse(ThreadLocalUtil.myObjectFinalized);
        ThreadLocalUtil.printMyValueFinalized();

        // did not expect this to be true. sun might do something clever to cascade null.
        assertTrue(ThreadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(ThreadLocalUtil.threadLocalUserFinalized);

        increaseMemoryThenGC();
        ThreadLocalUtil.printMyValueFinalized();
    }

    @Test
    public void test3() throws InterruptedException {
        Thread t = new ThreadExtension(new Runnable() {
            public void run() {
                for (int i=0;i<100;i++) {
                    ThreadLocalUser user = new ThreadLocalUser(i);
                    MyObject value = new MyObject(i);
                    user.setThreadLocal(value);

                    value = null;
                    user = null;
                    gc();
                }
            }
        });
        t.start();
        t.join();

        increaseMemoryThenGC();

        assertTrue(ThreadLocalUtil.myObjectFinalized);
        assertTrue(ThreadLocalUtil.threadLocalUserFinalized);
        assertTrue(ThreadLocalUtil.threadLocalExtensionFinalized);

        assertFalse(ThreadLocalUtil.threadExtensionFinalized);
    }

//    @Test
    public void test4() throws InterruptedException {
        Executor singlePool = Executors.newSingleThreadExecutor();
        singlePool.execute(new Runnable() {
            public void run() {
                ThreadLocalUser user = new ThreadLocalUser(1);
                MyObject value = new MyObject(1);
                user.setThreadLocal(value);
            }
        });
        Thread.sleep(100);
        increaseMemoryThenGC();

        assertFalse(ThreadLocalUtil.myObjectFinalized);
        ThreadLocalUtil.printMyValueFinalized();

        assertTrue(ThreadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(ThreadLocalUtil.threadLocalUserFinalized);

        increaseMemoryThenGC();
        ThreadLocalUtil.printMyValueFinalized();
    }

//    @Test
    public void test5() throws Exception {
        for (int i = 0; i < 100; i++) {
            ThreadLocalUser user = new ThreadLocalUser(i);
            MyObject value = new MyObject(i);
            user.setThreadLocal(value);
            value = null;
            user = null;
        }

        Thread.sleep(1000);
        gc();

        assertFalse(ThreadLocalUtil.myObjectFinalized);
        ThreadLocalUtil.printMyValueFinalized();

        assertTrue(ThreadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(ThreadLocalUtil.threadLocalUserFinalized);

        increaseMemoryThenGC();
        ThreadLocalUtil.printMyValueFinalized();
    }

    @Test
    public void shouldThreadLocalUserIsFinalizedFirstThenMyValueIsFinalized() throws Exception {
        for (int i = 0; i < 100; i++) {
            ThreadLocalUser user = new ThreadLocalUser(i);
            MyObject value = new MyObject(i);
            user.setThreadLocal(value);
            value = null;
            user = null;
            increaseMemoryThenGC();
        }

        assertFalse(ThreadLocalUtil.myObjectFinalized);
        ThreadLocalUtil.printMyValueFinalized();

        assertTrue(ThreadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(ThreadLocalUtil.threadLocalUserFinalized);

        increaseMemoryThenGC();
        ThreadLocalUtil.printMyValueFinalized();
    }
}

