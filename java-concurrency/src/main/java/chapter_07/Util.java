package chapter_07;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by vchhieng on 8/07/2015.
 */
public class Util {
    public static Callable<Integer> createCallableFinishIn(final long runningTime, final TimeUnit timeUnit) {
        final Callable<Integer> runnable = () -> {
            try {
                for (int i = 0; i < runningTime; i++) {
                    System.err.printf("%2d < %2d \n", i, runningTime);
                    Thread.sleep(timeUnit.toSeconds(1000));
                }
            } catch (Throwable throwable) {
                System.err.println("vmc:catching an exception");
                throw throwable;
            }
            return null;
        };
        return runnable;
    }
}
