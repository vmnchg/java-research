package chapter_07;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import util.StopWatch;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static junit.framework.Assert.assertTrue;

@RunWith(JUnit4.class)
public class _ExecutorServiceTest {

    @Test
    public void shouldOnlyRunTaskSequencetially() {
        _ExecutorService executorService = new _ExecutorService();
        StopWatch stopWatch = new StopWatch();
        for (int i = 0; i < executorService.NTHREADS * 2; i++) {
            // vmc: make thread work for 10 seconds.
            Callable<Integer> runnable= Util.createCallableFinishIn(10, TimeUnit.SECONDS);
            // vmc: cancel thread in 5 seconds using Future.
            stopWatch.start();
            try {
                executorService.submitCallableAndWait(runnable, 5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
            stopWatch.stop();
            assertTrue(5 <= stopWatch.toSecondValue());
        }
    }
}