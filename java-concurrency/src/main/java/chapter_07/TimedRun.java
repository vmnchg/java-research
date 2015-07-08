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

public class TimedRun {
    static final int NTHREADS = 2;
    private static final ScheduledExecutorService execScheduledService = Executors.newScheduledThreadPool(NTHREADS);
    private static final ExecutorService execService = Executors.newFixedThreadPool(NTHREADS);

    public Callable makeRunable(final int runningTime) {
        Callable<Integer> runnable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {
                    for (int i = 0; i < runningTime; i++) {
                        System.err.printf("%2d < %2d \n", i, runningTime);
                        Thread.sleep(TimeUnit.SECONDS.toSeconds(1000));
                    }
                } catch (Throwable throwable) {
                    System.err.println("vmc:catching an exception");
                    throw throwable;
                }
                return null;
            }
        };
        return runnable;
    }

    public void timedRunWithScheduledInteruption(final Callable<Integer> callable, long timeout, TimeUnit unit) throws InterruptedException {
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
        execScheduledService.schedule(new Runnable() {
            @Override
            public void run() {
                thread.interrupt();
            }
        }, timeout, unit);
        // vmc:need to make sure that interrupt is called before the thread join method.
        thread.join(unit.toMillis(2*timeout));
        task.rethrow();
    }

    public void timedRunWithFuture(final Callable<Integer> callable, long timeout, TimeUnit unit) {
        Future<?> ft = execService.submit(callable);

        try {
            ft.get(timeout, unit);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            ft.cancel(true);
        }
    }

    void shutdown() {
        // TODO Auto-generated method stub
        execScheduledService.shutdown();
        execService.shutdown();
    }
}
