package chapter_07;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.jcip.LaunderThrowable;

public class _ScheduledExecutorService {
    static final int NTHREADS = 2;
    private static final java.util.concurrent.ScheduledExecutorService execScheduledService = Executors.newScheduledThreadPool(NTHREADS);

    public void submitCallableWithInterruptable(final Callable<Integer> callable, long timeout, TimeUnit unit) throws InterruptedException {
        class RethrowableTask implements Runnable {
            volatile Throwable throwable = null;

            @Override
            public void run() {
                try {
                    callable.call();
                } catch (Throwable throwable) {
                    this.throwable = throwable;
                }
            }

            public void rethrow() {
                if (throwable != null)
                    throw LaunderThrowable.launderThrowable(throwable);
            }
        }

        final RethrowableTask task = new RethrowableTask();
        final Thread thread = new Thread(task);

        thread.start();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                thread.interrupt();
            }
        };
        execScheduledService.schedule(runnable, timeout, unit);
        // vmc:need to make sure that interrupt is called before the thread join method.
        thread.join(unit.toMillis(2 * timeout));
        task.rethrow();
    }

    void shutdown() {
        execScheduledService.shutdown();
    }
}
