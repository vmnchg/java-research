package threadlocal;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by vmc on 16/06/2015.
 * 164
 */

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ThreadLocalTest extends TestCase {
    private ThreadLocalUtil threadLocalUtil;
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
    public void before() {
        threadLocalUtil = new ThreadLocalUtil();
    }

    @After
    public void after() throws InterruptedException {
        Thread.sleep(1000);
    }

    @Test
    public void test1() {
        ThreadLocalUser user = new ThreadLocalUser(1, threadLocalUtil);
        MyObject value = new MyObject(1, threadLocalUtil);
        user.setThreadLocal(value);
        user.remove(); // vmc:this is where remove is called
        value = null;
        increaseMemoryThenGC();

        assertTrue(threadLocalUtil.myObjectFinalized);
        assertFalse(threadLocalUtil.threadLocalUserFinalized);
        assertFalse(threadLocalUtil.threadLocalExtensionFinalized);

        increaseMemoryThenGC();
        threadLocalUtil.printThreadLocalExtensionFinalized("test1");
        threadLocalUtil.printThreadLocalUserFinalized("test1");
    }

    @Test
    public void test2() {
        ThreadLocalUser user = new ThreadLocalUser(1, threadLocalUtil);
        MyObject value = new MyObject(1, threadLocalUtil);
        user.setThreadLocal(value);
        value = null;
        user = null; // this is where the nullify happens
        increaseMemoryThenGC();

        assertFalse(threadLocalUtil.myObjectFinalized);
        threadLocalUtil.printMyValueFinalized("test2");

        // did not expect this to be true. sun might do something clever to cascade null.
        assertTrue(threadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(threadLocalUtil.threadLocalUserFinalized);

        increaseMemoryThenGC();
        threadLocalUtil.printMyValueFinalized("test2");
    }

    @Test
    public void test3() throws InterruptedException {
        Thread t = new ThreadExtension(new Runnable() {
            public void run() {
                for (int i=0;i<100;i++) {
                    ThreadLocalUser user = new ThreadLocalUser(i, threadLocalUtil);
                    MyObject value = new MyObject(i, threadLocalUtil);
                    user.setThreadLocal(value);
                    gc();
                }
            }
        }, threadLocalUtil);
        t.start();
        t.join();

        increaseMemoryThenGC();

        assertTrue(threadLocalUtil.myObjectFinalized);
        assertTrue(threadLocalUtil.threadLocalUserFinalized);
        assertTrue(threadLocalUtil.threadLocalExtensionFinalized);

        assertFalse(threadLocalUtil.threadExtensionFinalized);
    }

    @Test
    public void test4() throws InterruptedException {
        Executor singlePool = Executors.newSingleThreadExecutor();
        singlePool.execute(new Runnable() {
            public void run() {
                ThreadLocalUser user = new ThreadLocalUser(1, threadLocalUtil);
                MyObject value = new MyObject(1, threadLocalUtil);
                user.setThreadLocal(value);
            }
        });
        Thread.sleep(100);
        increaseMemoryThenGC();

        assertFalse(threadLocalUtil.myObjectFinalized);
        threadLocalUtil.printMyValueFinalized("test4");

        assertTrue(threadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(threadLocalUtil.threadLocalUserFinalized);

        increaseMemoryThenGC();
        threadLocalUtil.printMyValueFinalized("test4");
    }

    @Test
    public void test5() throws Exception {
        for (int i = 0; i < 100; i++) {
            ThreadLocalUser user = new ThreadLocalUser(i, threadLocalUtil);
            MyObject value = new MyObject(i, threadLocalUtil);
            user.setThreadLocal(value);
            value = null;
            user = null;
        }

        gc();

        assertFalse(threadLocalUtil.myObjectFinalized);
        threadLocalUtil.printMyValueFinalized("test5");

        assertTrue(threadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(threadLocalUtil.threadLocalUserFinalized);

        increaseMemoryThenGC();
        threadLocalUtil.printMyValueFinalized("test5");
    }

    @Test
    public void test6() throws Exception {
        for (int i = 0; i < 100; i++) {
            ThreadLocalUser user = new ThreadLocalUser(i, threadLocalUtil);
            MyObject value = new MyObject(i, threadLocalUtil);
            user.setThreadLocal(value);
            value = null;
            user = null;
            increaseMemoryThenGC();
        }

        assertFalse(threadLocalUtil.myObjectFinalized);
        threadLocalUtil.printMyValueFinalized("test6");

        assertTrue(threadLocalUtil.threadLocalExtensionFinalized);
        assertTrue(threadLocalUtil.threadLocalUserFinalized);

        increaseMemoryThenGC();
        threadLocalUtil.printMyValueFinalized("test6");
    }
}

