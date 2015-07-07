package chapter_07;

import org.junit.Test;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by vchhieng on 7/07/2015.
 */
@RunWith(JUnit4.class)
public class LogServiceTest {
    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalExceptionWhenLogServiceIsStoppedAndMessagesAreSentToLogService() throws InterruptedException {
        int MESSAGESIZE = 10;
        LogService ls = new LogService(MESSAGESIZE);
        ls.start();
        ls.stop();
        ls.log("log.");
    }

    @Test
    public void should() throws InterruptedException {
        int MESSAGESIZE = 10;
        LogService ls = new LogService(MESSAGESIZE);
        ls.start();
        for (int i=0 ; i<20; i++) {
            ls.log(String.format("log.%2d\n", i));
        }
        ls.stop();
    }
}
