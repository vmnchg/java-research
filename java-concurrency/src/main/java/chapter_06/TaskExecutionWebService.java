package chapter_06;

import java.util.concurrent.*;

public class TaskExecutionWebService {
    private int numberOfThreads = 0;
    final ExecutorService execService;

    private final Executor executor;
    private final Executor execTaskWithNewThread;
    private final Executor execTaskWithExitingThread;

    public TaskExecutionWebService(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.execService = Executors.newFixedThreadPool(numberOfThreads);

        this.executor = Executors.newFixedThreadPool(this.numberOfThreads);
        this.execTaskWithNewThread = new ThreadPerTaskExecutor();
        this.execTaskWithExitingThread = new WithinThreadExecutor();
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