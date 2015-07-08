package futuretask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * 228
 */
@RunWith(JUnit4.class)
public class JobDiscovererTest {
    private List<String> messages = Collections.synchronizedList(new ArrayList<String>());

    @Test
    public void shouldGetOriginalTaskFromFutureTask() {
        final CountDownLatch latch = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            pool.submit(new Runnable() {
                public void run() {
                    final String identifier = " Runnable " + finalI;
                    try {
                        latch.await();
                    } catch (InterruptedException consumeAndExit) {
                        System.out.println(Thread.currentThread().getName() +
                                identifier + " was interrupted - exiting");
                    messages.add(identifier + " await but terminated task");
                    }
                }

                public String toString() {
                    return "Runnable: " + finalI;
                }
            });

            pool.submit(new Callable<String>() {
                public String call() throws InterruptedException {
                    final String identifier = " Callable " + finalI;
                    try {
                        latch.await();
                    } catch (InterruptedException consumeAndExit) {
                        System.out.println(Thread.currentThread().getName() +
                                identifier + " was interrupted - exiting");
                    messages.add(identifier + " await but terminated task");
                    }
                    return "success";
                }

                public String toString() {
                    return "Callable: " + finalI;
                }
            });
        }

        // Note: the Runnables returned from shutdownNow are NOT
        // the same objects as we submitted to the pool!!!
        List<Runnable> tasks = pool.shutdownNow();
        assertThat(tasks.size(), is(7));

        System.out.println("Tasks from ThreadPool");
        System.out.println("=====================");
        for (Runnable task : tasks) {
            System.out.println("Task from ThreadPool " + task);
            assertThat(task instanceof FutureTask, is(true));
        }

        System.out.println();
        System.out.println("Using our JobDiscoverer");
        System.out.println("=======================");

        for (Runnable task : tasks) {
            Object realTask = JobDiscoverer.findRealTask(task);
            System.out.println("Real task was actually " + realTask);
            assertThat(task instanceof Callable || task instanceof Runnable, is(true));
        }

        assertThat(messages.size(), is(3));
        for (String completedTask : messages) {
            System.out.println(completedTask);
        }
    }
}