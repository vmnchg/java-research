package chapter_06;

import org.junit.Test;

public class CompleteServiceTest {

    @Test
    public void shouldCompleteAllServicesWithASingleWorkerThread() {
        CompleteService completeService = new CompleteService(1);
        int total = completeService.sampleCompleteService(20);
        assertEquals("value should the same", 210, total);
    }

    @Test
    public void shouldCompleteAllServicesWith10WorkerThreads() {
        CompleteService completeService = new CompleteService(10);
        int total = completeService.sampleCompleteService(20);
        assertEquals("value should the same", 210, total);
    }
}