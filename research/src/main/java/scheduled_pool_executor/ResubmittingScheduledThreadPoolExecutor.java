package scheduled_pool_executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by vchhieng on 23/06/2015.
 */
public class ResubmittingScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {
    private final ScheduledExceptionHandler handler;

    ResubmittingScheduledThreadPoolExecutor(
            int poolSize, ScheduledExceptionHandler handler) {
        super(poolSize);
        this.handler = handler;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        ScheduledFuture<?> scheduledFuture = super.scheduleAtFixedRate(command, initialDelay, period, unit);
        handler.runnables.put(scheduledFuture, new FixedRateParameters(command, period, unit, handler.numberOfRuns));
        return scheduledFuture;
    }

    private void scheduleAtFixedRate(Runnable command, long period, TimeUnit unit, int numberOfRuns) {
        ScheduledFuture<?> scheduledFuture = super.scheduleAtFixedRate(command, period, period, unit);
        handler.runnables.put(scheduledFuture, new FixedRateParameters(command, period, unit, numberOfRuns));
    }

    @Override
    protected void afterExecute(Runnable runnable, Throwable t) {
        final ScheduledFuture future = (ScheduledFuture) runnable;
        // future.isDone() is always false for scheduled tasks,
        // unless there was an exception
        if (future.isDone()) {
            try {
                future.get();
            } catch (ExecutionException e) {
                final Throwable problem = e.getCause();
                if (problem != null) {
                    final FixedRateParameters fixedRateParameters = handler.runnables.remove(runnable); // it is really a future
                    if (fixedRateParameters == null) {
                        scheduleAtFixedRate(fixedRateParameters.command, fixedRateParameters.period,
                                fixedRateParameters.unit, fixedRateParameters.numberOfRuns);

                    } else {
                        boolean resubmitThisTask = handler.exceptionOccurred(problem);
                        if (resubmitThisTask) {
                            scheduleAtFixedRate(fixedRateParameters.command, fixedRateParameters.period,
                                    fixedRateParameters.unit, fixedRateParameters.numberOfRuns - 1);
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
