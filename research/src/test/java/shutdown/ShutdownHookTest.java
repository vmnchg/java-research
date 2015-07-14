package shutdown;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by vmc on 9/07/15.
 */
@RunWith(JUnit4.class)
public class ShutdownHookTest {

    @AfterClass
    public static void afterClass() {
        System.out.println("hello there");
    }

    @Test
    public void shouldAbleToCatchShutdownCommand() throws InterruptedException {
        ShutdownHook shutdownHook = new ShutdownHook();
        ShutdownHook shutdownHookSpy = spy(shutdownHook);

        shutdownHook.addShutdownHook();

//        killProcess();

        shutdownHook.notifyTaskIsFinished();
        verify(shutdownHookSpy, times(1)).shutdownCleanUpTask();
    }
}
