package shutdown;

import java.util.concurrent.CountDownLatch;

/**
 * Created by vmc on 9/07/15.
 */
public class ShutdownHook {
    public void addShutdownHook() throws InterruptedException {
        CountDownLatch countDownLatch=new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutdown hook ran!");
            }
        });

        while (true) {
            Thread.sleep(1000);
        }
    }
}
