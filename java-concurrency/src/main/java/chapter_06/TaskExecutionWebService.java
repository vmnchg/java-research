package chapter_06;

import java.util.concurrent.*;

public class TaskExecutionWebService {
    private int numberOfThreads = 0;

    ExecutorService execService;

    private Executor exec;
    private final Executor execTaskWithNewThread;
    private final Executor execTaskWithExitingThread;

    public TaskExecutionWebService(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.execService = Executors.newFixedThreadPool(numberOfThreads);

        this.exec = Executors.newFixedThreadPool(this.numberOfThreads);
        this.execTaskWithNewThread = new ThreadPerTaskExecutor();
        this.execTaskWithExitingThread = new WithinThreadExecutor();
    }

    public void executor_callable(Callable<Object> task) {
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

    public void executor_task(Runnable task) {
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