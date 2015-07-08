package chapter_07;

import java.util.concurrent.*;

/**
 * Created by vchhieng on 8/07/2015.
 */
public class _ExecutorService {
    static final int NTHREADS = 2;
    private static final ExecutorService execService = Executors.newFixedThreadPool(NTHREADS);

    public void submitCallableAndWait(final Callable<Integer> callable, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Future<Integer> ft = execService.submit(callable);

        try {
            final Integer result = ft.get(timeout, unit);
        } catch (Throwable e) {
            throw e;
        } finally {
            ft.cancel(true);
        }
    }

    void shutdown() {
        execService.shutdown();
    }
}
