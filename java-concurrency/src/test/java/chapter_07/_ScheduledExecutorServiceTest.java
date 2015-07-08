package chapter_07;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by vchhieng on 8/07/2015.
 */
@RunWith(JUnit4.class)
public class _ScheduledExecutorServiceTest {

    @Test (expected = IllegalStateException.class)
    public void shouldTimeCorrectly() {
        _ScheduledExecutorService scheduledExecutorService = new _ScheduledExecutorService();
        // vmc: make thread work for 10 seconds.
        Callable<Integer> runnable = Util.createCallableFinishIn(10, TimeUnit.SECONDS);
        try {
            // vmc: cancel thread in 3 seconds.
            scheduledExecutorService.submitCallableWithInterruptable(runnable, 3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
