package date_format_172;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.*;

@RunWith(JUnit4.class)
public class DateConverterTest {
    private static void convert(ExecutorService pool, final String date) {
        pool.submit(new Runnable() {
            public void run() {
                DateConverter dc = new DateConverter();
                while (true) {
                    dc.testConvert(date);
                }
            }
        });
    }

    @Test
    public void shouldHaveWongkyDate() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        convert(pool, "1971/12/04");
        convert(pool, "2001/09/02");

        Thread.currentThread().sleep(5000);
    }
}

