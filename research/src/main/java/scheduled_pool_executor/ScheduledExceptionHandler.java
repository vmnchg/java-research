package scheduled_pool_executor;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by vchhieng on 23/06/2015.
 */
public abstract class ScheduledExceptionHandler {
    protected final Map<Object, FixedRateParameters> runnables = new IdentityHashMap<Object, FixedRateParameters>();
    protected final int numberOfRuns;

    public ScheduledExceptionHandler(int numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
    }


    /**
     * @return true if the task should be rescheduled;
     *         false otherwise
     */
    abstract boolean exceptionOccurred(Throwable e);
}
