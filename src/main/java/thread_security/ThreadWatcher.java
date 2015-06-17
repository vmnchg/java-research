package thread_security;

import java.security.Permission;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by vchhieng on 17/06/2015.
 */
public class ThreadWatcher extends SecurityManager {
    private final Predicate<Thread> predicate;
    private final Consumer<Thread> action;

    public ThreadWatcher(Predicate<Thread> predicate,
                         Consumer<Thread> action) {
        this.predicate = predicate;
        this.action = action;
    }

    public void checkPermission(Permission perm) {
        // allow everything
    }

    public void checkPermission(Permission perm, Object context) {
        // allow everything
    }

    public void checkAccess(ThreadGroup g) {
        Thread creatingThread = Thread.currentThread();
        if (predicate.test(creatingThread)) {
            action.accept(creatingThread);
        }
    }
}
