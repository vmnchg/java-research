package scheduled_pool_executor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by vchhieng on 23/06/2015.
 */
@RunWith(JUnit4.class)
public class ResubmittingScheduledThreadPoolExecutorTest {
    private static class MyRunnable implements Runnable {
        public void run() {
           System.out.println("I'm happy");
        }
    }

    private static class MyExceptionRunnable implements Runnable {
        private final String identifier;

        private MyExceptionRunnable(String identifier) {
            this.identifier = identifier;
        }

        public void run() {
            throw new IllegalStateException(identifier);
        }
    }

    private static class MyScheduledExceptionHandler
            implements ScheduledExceptionHandler {
        private AtomicInteger problems = new AtomicInteger();

        public boolean exceptionOccurred(Throwable e) {
            e.printStackTrace();
            if (problems.incrementAndGet() >= 5) {
                System.err.println("We give up!");
                return false;
            }
            System.err.println("Resubmitting task to scheduler");
            return true;
        }
    }

    @Test
    public void shouldHaveNoProblem() throws InterruptedException {
        final MyScheduledExceptionHandler exceptionHandler = new MyScheduledExceptionHandler();
        final int initialDelay = 1;
        final int numberOfRetry = 5;
        final int period = 2;

        final ScheduledExecutorService resubmittingScheduledThreadPoolExecutor = new ResubmittingScheduledThreadPoolExecutor(
                numberOfRetry, exceptionHandler);

        resubmittingScheduledThreadPoolExecutor.scheduleAtFixedRate(new MyRunnable(), initialDelay, period, TimeUnit.SECONDS);

        Thread.sleep(TimeUnit.SECONDS.toMillis(1));

        assertThat(exceptionHandler.problems.intValue(), is(0));
    }

    @Test
    public void shouldRecover5Times() throws InterruptedException {
        final MyScheduledExceptionHandler exceptionHandler = new MyScheduledExceptionHandler();
        final int initialDelay = 1;
        final int numberOfRetry = 5;
        final int period = 1;

        final ScheduledExecutorService resubmittingScheduledThreadPoolExecutor = new ResubmittingScheduledThreadPoolExecutor(
                numberOfRetry, exceptionHandler);

        resubmittingScheduledThreadPoolExecutor.scheduleAtFixedRate(new MyExceptionRunnable("I have a problem"), initialDelay, period, TimeUnit.SECONDS);

        Thread.sleep(TimeUnit.SECONDS.toMillis(6 + 1));

        assertThat(exceptionHandler.problems.get(), is(numberOfRetry));
    }
}