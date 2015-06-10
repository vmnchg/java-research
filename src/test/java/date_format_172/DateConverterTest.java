package date_format_172;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class DateConverterTest {
    static ExecutorService pool = Executors.newCachedThreadPool();
    static AtomicInteger atomicInteger = new AtomicInteger();

    private static void submitToPool(ExecutorService pool, final String date) {
        pool.submit(new Runnable() {
            public void run() {
                DateConverter dc = new DateConverter();
                while (true) {
                    try {
                        dc.convert(date);
                    } catch (IllegalStateException e) {
                        atomicInteger.incrementAndGet();
                    }
                }
            }
        });
    }

    @Test
    public void shouldHaveWongkyDate() throws InterruptedException {
        submitToPool(pool, "1971/12/04");
        for (int i=0; i<50; i++) {
            Thread.currentThread().sleep(100);
            assertThat(atomicInteger.get(), is(0));
        }
    }

    @Test
    public void shouldThrowException() throws InterruptedException {
        submitToPool(pool, "1971/12/04");
        submitToPool(pool, "2001/09/02");
        for (int i=0; i<50; i++) {
            Thread.currentThread().sleep(100);
        }
        assertThat(atomicInteger.get(), not(is(0)));
    }
}