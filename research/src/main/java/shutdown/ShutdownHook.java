package shutdown;

import java.util.concurrent.CountDownLatch;

/**
 * Created by vmc on 9/07/15.
 */
public class ShutdownHook {
    final CountDownLatch countDownLatch = new CountDownLatch(1);

    public void addShutdownHook() throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("start cleanup task");
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                shutdownCleanUpTask();
            }
        });
    }

    void shutdownCleanUpTask() {
        System.out.println("finish cleanup task");
    }

    void notifyTaskIsFinished() {
        countDownLatch.countDown();
    }
}
