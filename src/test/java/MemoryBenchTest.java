/**
 * Created by vchhieng on 22/05/2015.
 */

import benchmark.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by vchhieng on 5/05/2015.
 */
@RunWith(JUnit4.class)
public class MemoryBenchTest {
    @Test
    public void shouldBasicObjectFactory() {
        MemoryBench m = new MemoryBench();
        m.showMemoryUsage(new BasicObjectFactory());
    }

    @Test
    public void shouldByteFactory() {
        MemoryBench m = new MemoryBench();
        m.showMemoryUsage(new ByteFactory());
    }

    @Test
    public void shouldThreeByteFactory() {
        MemoryBench m = new MemoryBench();
        m.showMemoryUsage(new ThreeByteFactory());
    }

    @Test
    public void shouldSixtyFourBooleanFactory() {
        MemoryBench m = new MemoryBench();
        m.showMemoryUsage(new SixtyFourBooleanFactory());
    }
}
