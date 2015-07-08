package shutdown;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by vmc on 9/07/15.
 */
@RunWith(JUnit4.class)
public class ShutdownHookTest {

    @Test
    public void shouldAbleToCatchShutdownCommand() throws InterruptedException {
        ShutdownHook shutdownHook = new ShutdownHook();
        shutdownHook.addShutdownHook();
    }
}
