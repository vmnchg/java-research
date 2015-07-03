package thread;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Arrays;
import static junit.framework.TestCase.assertTrue;

@RunWith(JUnit4.class)
public class ThreadMemoryTest {

    @Test
    public void shouldCalculateMemoryPerThread() {
        long subStringPoorPerformance = performSubStringPoorPerformance();
        long subStringOptimal = performSubStringOptimal();
        assertTrue(subStringOptimal < subStringPoorPerformance);
    }

    private long performSubStringOptimal() {
        char[] largeText = new char[10000000];
        Arrays.fill(largeText, 'A');
        CharSequence superString = new SubbableString(largeText);
        long bytes = ThreadMemory.threadAllocatedBytes();
        CharSequence[] subStrings = new CharSequence[
                largeText.length / 1000];
        for (int i = 0; i < subStrings.length; i++) {
            subStrings[i] = superString.subSequence(
                    i * 1000, i * 1000 + 1000);
        }
        bytes = ThreadMemory.threadAllocatedBytes() - bytes;
        System.out.println(String.format("current %d\n", ThreadMemory.threadAllocatedBytes()));
        return bytes;
    }

    private long performSubStringPoorPerformance() {
        char[] largeText = new char[10 * 1000 * 1000];
        Arrays.fill(largeText, 'A');
        String superString = new String(largeText);

        long bytes = ThreadMemory.threadAllocatedBytes();
        String[] subStrings = new String[largeText.length / 1000];
        for (int i = 0; i < subStrings.length; i++) {
            subStrings[i] = superString.substring(
                    i * 1000, i * 1000 + 1000);
        }
        bytes = ThreadMemory.threadAllocatedBytes() - bytes;
        System.out.println(String.format("current %d\n", ThreadMemory.threadAllocatedBytes()));
        return bytes;
    }
}