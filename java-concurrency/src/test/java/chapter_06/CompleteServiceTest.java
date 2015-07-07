package chapter_06;

import org.junit.Test;

public class CompleteServiceTest {
    @Test
    public void shouldCompleteAllService() {
        CompleteService completeService = new CompleteService();
        completeService.sampleCompleteService(20);
    }
}