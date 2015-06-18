package thread_security;

/**
 * Created by vchhieng on 17/06/2015.
 */
import java.util.function.*;

public interface DemoSupport {
    static Predicate<Thread> createPredicate() {
        return (Thread t) ->
                t.getName().matches("pool-\\d+-thread-\\d+");
    }

    static Consumer<Thread> createConsumer() {
        return (Thread creator) -> {
            throw new SecurityException(creator +
                    " tried to create a thread");
        };
    }

}
