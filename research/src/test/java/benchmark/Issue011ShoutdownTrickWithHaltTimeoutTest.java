package benchmark;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by vchhieng on 5/05/2015.
 */
@RunWith(JUnit4.class)
public class Issue011ShoutdownTrickWithHaltTimeoutTest {
    @Test
    public void shouldNotShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("You wanna quit, hey?");
                System.out.println("... fry eggs on your CPU.");
                while(true);
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch(InterruptedException ex) {}
                // halt will bail out without calling further shutdown hooks or
                // finalizers
                Runtime.getRuntime().halt(1);
            }
        });
        System.out.println("Let's take a break...");
        try {
            Thread.sleep(5000);
        }
        catch(InterruptedException ex) {}
        System.out.println("That's it, I'm outta here");
        System.exit(0);
        System.out.println("This line will not show!");
    }
}
