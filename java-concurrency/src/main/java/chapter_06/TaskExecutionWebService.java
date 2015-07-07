package chapter_06;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskExecutionWebService {
    private static final int             NTHREADS                  = 10;

    private static final Executor        exec                      = Executors.newFixedThreadPool(NTHREADS);
    private static final ExecutorService execService               = Executors.newFixedThreadPool(NTHREADS);

    private static final Executor        execTaskWithNewThread     = new ThreadPerTaskExecutor();
    private static final Executor        execTaskWithExitingThread = new WithinThreadExecutor();

    /*
     * public static void executor_futuretask(Callable<Object> task) {
     * FutureTask ft = execService.submit(task); }
     */

    public static void executor_callable(Callable<Object> task) {
        Future<Object> future = execService.submit(task);
        try {
            Object object = future.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void executor_task(Runnable task) {
        Future<?> future = execService.submit(task);
        try {
            Object object = future.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void executor_shutingDown() {
        for (int i = 0; i < 100; i++) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    System.out.print(".");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Someone interupt me");
                    }
                }
            };
            execService.execute(task);
        }

        try {
            System.out.println();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("%s %s \n", "Is execution pool shuting down ? =  ", execService.isShutdown());
        // execService.shutdown();
        try {
            /**
             * vmc:shutdownNow() will try to shutdown the thread immediately.
             */
            System.out.println("Remaining Task  " + execService.shutdownNow().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("%s %s \n", "Is execution pool shuting down ? =  ", execService.isShutdown());
    }

    public static void main(String[] args) throws IOException {
        executor_shutingDown();
    }
}

// Start its own thread.
class ThreadPerTaskExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}

// Will use the existing thread for execution.
class WithinThreadExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}