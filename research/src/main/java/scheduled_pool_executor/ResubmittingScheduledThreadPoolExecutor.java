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
    /** Default exception handler, always reschedules */
    private static final ScheduledExceptionHandler NULL_HANDLER =
            new ScheduledExceptionHandler() {
                public boolean exceptionOccurred(Throwable e) {
                    return true;
                }
            };
    private final Map<Object, FixedRateParameters> runnables =
            new IdentityHashMap<Object, FixedRateParameters>();
    private final ScheduledExceptionHandler handler;

    /**
     */
    public ResubmittingScheduledThreadPoolExecutor(int poolSize) {
        this(poolSize, NULL_HANDLER);
    }

    ResubmittingScheduledThreadPoolExecutor(
            int poolSize, ScheduledExceptionHandler handler) {
        super(poolSize);
        this.handler = handler;
    }

    private class FixedRateParameters {
        private Runnable command;
        private long period;
        private TimeUnit unit;

        /**
         * We do not need initialDelay, since we can set it to period
         */
        public FixedRateParameters(Runnable command, long period,
                                   TimeUnit unit) {
            this.command = command;
            this.period = period;
            this.unit = unit;
        }
    }

    public ScheduledFuture<?> scheduleAtFixedRate(
            Runnable command, long initialDelay, long period,
            TimeUnit unit) {
        ScheduledFuture<?> future = super.scheduleAtFixedRate(
                command, initialDelay, period, unit);
        runnables.put(future,
                new FixedRateParameters(command, period, unit));
        return future;
    }

    protected void afterExecute(Runnable r, Throwable t) {
        ScheduledFuture future = (ScheduledFuture) r;
        // future.isDone() is always false for scheduled tasks,
        // unless there was an exception
        if (future.isDone()) {
            try {
                future.get();
            } catch (ExecutionException e) {
                Throwable problem = e.getCause();
                FixedRateParameters parms = runnables.remove(r);
                if (problem != null && parms != null) {
                    boolean resubmitThisTask =
                            handler.exceptionOccurred(problem);
                    if (resubmitThisTask) {
                        scheduleAtFixedRate(parms.command, parms.period,
                                parms.period, parms.unit);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
