package thread;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Created by vchhieng on 3/07/2015.
 */
public class ThreadMemory {

    private static final String GET_THREAD_ALLOCATED_BYTES = "getThreadAllocatedBytes";

    public static long threadAllocatedBytes() {
        try {
            return (Long) ManagementFactory.getPlatformMBeanServer()
                    .invoke(new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME),
                            GET_THREAD_ALLOCATED_BYTES,
                            new Object[]{Thread.currentThread().getId()},
                            new String[]{long.class.getName()}
                    );
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
