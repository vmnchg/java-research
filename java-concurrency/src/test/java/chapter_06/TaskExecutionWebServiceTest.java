package chapter_06;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by vchhieng on 7/07/2015.
 */
@RunWith(JUnit4.class)
public class TaskExecutionWebServiceTest {
    TaskExecutionWebService taskExecutionWebService;
    AtomicInteger numberOfInterruptedTasks = new AtomicInteger(0);

    @Before
    public void before() {
        numberOfInterruptedTasks.set(0);
    }

    @Test
    public void shouldTheNumberOfConcurrentExecutableTaskBeTheLargerOrEqualToAsTheNumberOfInterruptedTaskWhenShutdownIsCalled() throws Exception {
        final int numberOfConcurrentExecutableTasks = 10;
        taskExecutionWebService = new TaskExecutionWebService(numberOfConcurrentExecutableTasks);
        scheduleTasks();

        sleep(SECONDS.toMillis(1));
        System.out.printf("%s %s \n", "Is execution pool shuting down ? =  ", taskExecutionWebService.execService.isShutdown());
        try {
            /**
             * vmc:shutdownNow() will try to shutdown the thread immediately.
             */
            System.out.println("Remaining Task  " + taskExecutionWebService.execService.shutdownNow().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        sleep(SECONDS.toMillis(1));

        System.out.printf("%s %s \n", "Is execution pool shutting down ? =  ", taskExecutionWebService.execService.isShutdown());
        assertTrue("there should be at least one interrupted task", 1 <= numberOfInterruptedTasks.get());
        assertTrue("number of concurrent executable tasks should be larger or equal to number of interrupted task when shutdown is called",
                numberOfInterruptedTasks.get() <= numberOfConcurrentExecutableTasks);
    }

    @Test
    public void shouldTheNumberOfConcurrentExecutableTaskBeZeroWhenShutdownIsNotCalled() throws Exception {
        final int numberOfConcurrentExecutableTasks = 10;
        taskExecutionWebService = new TaskExecutionWebService(numberOfConcurrentExecutableTasks);
        scheduleTasks();

        System.out.printf("%s %s \n", "Is execution pool shutting down ? =  ", taskExecutionWebService.execService.isShutdown());
        assertTrue("there should be not interrupted task", numberOfInterruptedTasks.get() == 0);
        assertTrue("number of concurrent executable tasks should be larger or equal to number of interrupted task when shutdown is called",
                numberOfInterruptedTasks.get() <= numberOfConcurrentExecutableTasks);
    }

    private void sleep(long millis) {
        try {
            System.out.println();
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void scheduleTasks() {
        for (int i = 0; i < 100; i++) {
            final int finalI = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    System.out.print(String.format("runnable[%d] begin ", finalI));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.print(String.format("runnable[%d] someone interrupt me \n", finalI));
                        numberOfInterruptedTasks.getAndIncrement();
                    }
                    System.out.print(String.format("runnable[%d] end \n", finalI));
                }
            };
            taskExecutionWebService.execService.execute(task);
        }
    }
}
