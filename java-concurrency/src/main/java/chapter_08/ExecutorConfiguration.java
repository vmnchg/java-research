package chapter_08;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class ExecutorConfiguration {
    ThreadPoolExecutor executor = null;

    public ExecutorConfiguration(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workqueue) {

        executor = new TimingThreadPool(corePoolSize, maximumPoolSize, keepAliveTime, unit, workqueue);

        // Gracefully push back the flow of data back to the client.
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public Callable<Integer> makeCallable(final int runningTime) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                // TODO Auto-generated method stub
                for (int i = 0; i < runningTime; i++) {
                    Thread.sleep(TimeUnit.SECONDS.toSeconds(1000));
                    System.out.print(".");
                }
                System.out.println();
                return new Integer(runningTime);
            };

        };
        return callable;
    }

    class TimingThreadPool extends ThreadPoolExecutor {
        private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
        private final Logger            log       = Logger.getLogger(TimingThreadPool.class.getName());
        private final AtomicLong        numTasks  = new AtomicLong();
        private final AtomicLong        totalTime = new AtomicLong();

        public TimingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            log.fine(String.format("Thread %s: start %s", t, r));
            startTime.set(System.nanoTime());

        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            try {
                long endTime = System.nanoTime();
                long taskTime = endTime - startTime.get();
                numTasks.incrementAndGet();
                totalTime.addAndGet(taskTime);
                log.fine(String.format("Thread %s: end %s time=%dns", t, r, taskTime));
            } finally {
                super.afterExecute(r, t);
            }
        }

        @Override
        protected void terminated() {
            try {
                log.info(String.format("Terminated: avg time=%dns", totalTime.get() / numTasks.get()));
            } finally {
                super.terminated();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>();
        ExecutorConfiguration executor = new ExecutorConfiguration(10, 10, 2, TimeUnit.SECONDS, blockingQueue);

        List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
        int numberOfCallable = 10;
        for (int i = 1; i <= numberOfCallable; i++) {
            tasks.add(executor.makeCallable(i));
        }

        List<Future<Integer>> fs = executor.executor.invokeAll(tasks);

        System.out.println("<Done>");
        for (int i = 0; i < numberOfCallable; i++) {
            Future<Integer> f = fs.get(i);
            Integer integer = null;
            try {
                integer = f.get();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                System.out.println("Integer=" + integer);
            }
        }
        System.out.println("</Done>");
        //executor.executor.shutdown();
    }
}
