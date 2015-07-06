package chapter_07;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.jcip.LaunderThrowable;


public class TimedRun {
    private static final int                      NTHREADS             = 2;
    private static final ScheduledExecutorService execScheduledService = Executors.newScheduledThreadPool(NTHREADS);
    private static final ExecutorService          execService          = Executors.newFixedThreadPool(NTHREADS);

    public Runnable makeRunable(final int runningTime) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {

                try {
                    for (int i = 0; i < runningTime; i++) {
                        System.out.printf(" [%2d] ", i);

                        Thread.sleep(TimeUnit.SECONDS.toSeconds(1000));
                    }
                } catch (InterruptedException e) {
                    System.out.println("vmc:catching the interupted exception");
                    e.printStackTrace();
                }
            }
        };
        return runnable;
    }

    public void timedRunWithScheduledInteruption(final Runnable r, long timeout, TimeUnit unit)
    throws InterruptedException {
        class RethrowableTask implements Runnable {
            volatile Throwable t = null;

            @Override
            public void run() {
                try {
                    r.run();
                } catch (Throwable t) {
                    this.t = t;
                }
            }

            public void rethrow() {
                if (t != null)
                    throw LaunderThrowable.launderThrowable(t);
            }
        }

        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);

        taskThread.start();
        execScheduledService.schedule(new Runnable() {
            @Override
            public void run() {
                taskThread.interrupt();
            }
        }, timeout, unit);

        System.out.println("Here1");
        taskThread.join(unit.toMillis(timeout));
        System.out.println("Here2");
        task.rethrow();
        System.out.println("Here3");
    }

    public void timedRunWithFuture(final Runnable r, long timeout, TimeUnit unit) {
        Future<?> ft = execService.submit(r);

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

    private void shutdown() {
        // TODO Auto-generated method stub
        execScheduledService.shutdown();
        execService.shutdown();
    }

    private void testing() {
        ThreadPoolExecutor t = null;
        Callable c = null;
        FutureTask ft = null;
    }

    public static void main(String[] args) throws InterruptedException {
        TimedRun tr = new TimedRun();
        // vmc: make thread work for 10 seconds.
        Runnable runnable = tr.makeRunable(10);
        // vmc: cancel thread in 3 seconds.
        tr.timedRunWithScheduledInteruption(runnable, 3, TimeUnit.SECONDS);

        for (int i = 0; i < tr.NTHREADS * 2; i++) {
            // vmc: make thread work for 10 seconds.
            runnable = tr.makeRunable(10);
            // vmc: cancel thread in 5 seconds using Future.
            tr.timedRunWithFuture(runnable, 5, TimeUnit.SECONDS);
        }

        System.err.println("<shutdown>");
        tr.shutdown();
        System.err.println("</shutdown>");
    }
}
