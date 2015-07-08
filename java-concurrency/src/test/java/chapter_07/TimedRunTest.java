package chapter_07;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.IllegalFormatCodePointException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by vchhieng on 8/07/2015.
 */
@RunWith(JUnit4.class)
public class TimedRunTest {

    @Test (expected = IllegalStateException.class)
    public void shouldTimeCorrectly() {
        TimedRun timedRun = new TimedRun();
        // vmc: make thread work for 10 seconds.
        Callable<Integer> runnable = timedRun.makeRunable(10);
        try {
            // vmc: cancel thread in 3 seconds.
            timedRun.timedRunWithScheduledInteruption(runnable, 3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldRunmore() {
        TimedRun timedRun = new TimedRun();
        for (int i = 0; i < timedRun.NTHREADS * 2; i++) {
            // vmc: make thread work for 10 seconds.
            Callable<Integer> runnable= timedRun.makeRunable(10);
            // vmc: cancel thread in 5 seconds using Future.
            timedRun.timedRunWithFuture(runnable, 5, TimeUnit.SECONDS);
        }

        System.err.println("<shutdown>");
        timedRun.shutdown();
        System.err.println("</shutdown>");
    }
}
