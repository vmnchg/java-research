package date_format;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class DateConverterTest {
    private ExecutorService pool;
    private AtomicInteger atomicInteger = new AtomicInteger();

    private void submitToPool(final String date, final Boolean multiThreads) {
        pool = Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        if (multiThreads) {
                            DateConverter.convertThreadSafe(date);
                        } else {
                            DateConverter.convert(date);
                        }
                    } catch (IllegalStateException e) {
                        atomicInteger.incrementAndGet();
                    }
                }
            }
        });
    }

    @Before
    public void before() {
        atomicInteger.set(0);
    }

    @After
    public void after() {
        pool.shutdown();
        boolean forcedShutdown = false;
        while (true) {
            if (pool.isShutdown() || forcedShutdown) {
                break;
            }
        }
    }

    @Test
    public void shouldHaveWongkyDate() {
        assertThat(String.format("atomicInteger %d", atomicInteger.get()), atomicInteger.get(), is(0));
        submitToPool("1971/12/04", false);
        for (int i = 0; i < 50; i++) {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertThat(String.format("atomicInteger %d", atomicInteger.get()), atomicInteger.get(), is(0));
    }

    @Test
    public void shouldThrowException() {
        assertThat(String.format("atomicInteger %d", atomicInteger.get()), atomicInteger.get(), is(0));
        submitToPool("1971/12/04", false);
        submitToPool("2001/09/02", false);
        submitToPool("2002/09/02", true);
        for (int i = 0; i < 50; i++) {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertThat(String.format("atomicInteger %d", atomicInteger.get()), atomicInteger.get(), not(is(0)));
    }

    @Test
    public void shouldConvertDateSafely()  {
        assertThat(String.format("atomicInteger %d", atomicInteger.get()), atomicInteger.get(), is(0));
        submitToPool("1971/12/04", true);
        submitToPool("2001/09/02", true);
        submitToPool("2002/09/02", true);
        for (int i = 0; i < 50; i++) {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertThat(String.format("atomicInteger %d", atomicInteger.get()), atomicInteger.get(), is(0));
    }
}