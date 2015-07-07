package chapter_06;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * This is used when a task must be complete before proceed
 */
@RunWith(JUnit4.class)
public class CompleteServiceTest {
    @Test
    public void shouldCompleteAllService() {
        CompleteService completeService = new CompleteService();
        completeService.sampleCompleteService(20);
    }
}