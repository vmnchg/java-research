package scheduled_pool_executor;

/**
 * Created by vchhieng on 23/06/2015.
 */
public interface ScheduledExceptionHandler {
    /**
     * @return true if the task should be rescheduled;
     *         false otherwise
     */
    boolean exceptionOccurred(Throwable e);
}
