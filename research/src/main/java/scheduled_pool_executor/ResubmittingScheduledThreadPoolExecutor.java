package scheduled_pool_executor;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by vchhieng on 23/06/2015.
 */
public class ResubmittingScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {
    /**
     * Default exception handler, always reschedules
     */
    private static final ScheduledExceptionHandler NULL_HANDLER = new ScheduledExceptionHandler() {
        public boolean exceptionOccurred(Throwable e) {
            return true;
        }
    };

    private final Map<Object, FixedRateParameters> runnables = new IdentityHashMap<Object, FixedRateParameters>();

    private final ScheduledExceptionHandler handler;

    /**
     */
    private ResubmittingScheduledThreadPoolExecutor(int poolSize) {
        this(poolSize, NULL_HANDLER);
    }

    ResubmittingScheduledThreadPoolExecutor(
            int poolSize, ScheduledExceptionHandler handler) {
        super(poolSize);
        this.handler = handler;
    }

    private class FixedRateParameters {
        final private Runnable command;
        final private long period;
        final private TimeUnit unit;
        final private int numberOfRuns;

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

    private ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit, int numberOfRuns) {
        final ScheduledFuture<?> future = super.scheduleAtFixedRate(command, initialDelay, period, unit);
        runnables.put(future, new FixedRateParameters(command, period, unit, numberOfRuns));
        return future;
    }

    protected void afterExecute(Runnable runnable, Throwable t) {
        final ScheduledFuture future = (ScheduledFuture) runnable;
        // future.isDone() is always false for scheduled tasks,
        // unless there was an exception
        if (future.isDone()) {
            try {
                future.get();
            } catch (ExecutionException e) {
                final Throwable problem = e.getCause();
                final FixedRateParameters fixedRateParameters = runnables.remove(runnable); // it is really a future
                if (problem != null && fixedRateParameters != null) {
                    boolean resubmitThisTask =
                            handler.exceptionOccurred(problem);
                    if (resubmitThisTask) {
                        scheduleAtFixedRate(fixedRateParameters.command, fixedRateParameters.period,
                                fixedRateParameters.period, fixedRateParameters.unit, fixedRateParameters.numberOfRuns - 1);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
