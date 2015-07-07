package scheduled_pool_executor;

import java.util.concurrent.TimeUnit;

/**
 * Created by vmc on 2/07/15.
 */
public class FixedRateParameters {
    protected final Runnable command;
    protected final long period;
    protected final TimeUnit unit;
    protected final int numberOfRuns;

    /**
     * We do not need initialDelay, since we can set it to period
     */
    public FixedRateParameters(Runnable command, long period, TimeUnit unit, int numberOfRuns) {
        this.command = command;
        this.period = period;
        this.unit = unit;
        this.numberOfRuns = numberOfRuns;
    }
}