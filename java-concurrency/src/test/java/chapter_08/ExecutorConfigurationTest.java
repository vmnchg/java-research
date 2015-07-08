package chapter_08;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import util.StopWatch;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by vmc on 8/07/15.
 */
@RunWith(JUnit4.class)
public class ExecutorConfigurationTest {
    private ExecutorConfiguration executorConfiguration;

    private int corePoolSize = 10;
    private int maximumPoolSize = 10;
    private int keepAliveTime = 2;

    @Before
    public void before() {
        executorConfiguration = new ExecutorConfiguration(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Test
    public void shouldWaitForTheCompletionOfAllTasksWhenInvokeAllIsUsed() throws InterruptedException {
        List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
        int numberOfCallable = 10;
        int sleepingTime = 1000;
        for (int i = 1; i <= numberOfCallable; i++) {
            tasks.add(executorConfiguration.makeCallable(i, sleepingTime));
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Future<Integer>> fs = executorConfiguration.executor.invokeAll(tasks);
        stopWatch.stop();

        assertTrue("main thread should be blocked until invokeAll finish", 5 <= stopWatch.toSecondValue());

        int total = 0;
        for (int i = 0; i < numberOfCallable; i++) {
            Future<Integer> f = fs.get(i);
            Integer integer = null;
            try {
                integer = f.get();
                total += integer;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Integer=" + integer);
            }
        }
        assertThat(total, is(55));
    }
}
