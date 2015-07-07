package thread_security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by vchhieng on 17/06/2015.
 * 226
 */
@RunWith(JUnit4.class)
public class ThreadWatcherTest {

    static Runnable createHelloJob() {
        return () -> System.out.printf(
                "Hello from \"%s\"",
                Thread.currentThread()
        );
    }

    @Test
    public void shouldCreateThreadOk() {
        System.setSecurityManager(
                new ThreadWatcher(
                        DemoSupport.createPredicate(),
                        DemoSupport.createConsumer()
                )
        );

        new Thread(createHelloJob(),
                "This should work").start();

        System.setSecurityManager(null);

    }

    @Test
    public void shouldReceiveException() {
        System.setSecurityManager(
                new ThreadWatcher(
                        DemoSupport.createPredicate(),
                        DemoSupport.createConsumer()
                )
        );

        ExecutorService pool = Executors.newFixedThreadPool(10);
        Future<?> future = pool.submit(() ->
                        new Thread(createHelloJob(),
                                "This should print a warning 1")
        );
        try {
            future.get();
        } catch (ExecutionException e) {
            e.getCause().printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();

        System.setSecurityManager(null);
    }

    @Test
    public void shouldKeepTrackOfNumberOfMissBehavedThread() {
        final ThreadAccumulator action = new ThreadAccumulator();
        System.setSecurityManager(
                new ThreadWatcher(
                        DemoSupport.createPredicate(),
                        action
                )
        );

        ExecutorService pool = Executors.newFixedThreadPool(10);
        Future<?> future = pool.submit(() ->
                        new Thread(createHelloJob(),
                                "This should print a warning 1")
        );
        try {
            future.get();
        } catch (ExecutionException e) {
            e.getCause().printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        System.out.println("getNumberOfMissBehavedThread " + action.getNumberOfMisbehavingThreads());
        System.setSecurityManager(null);
    }
}
