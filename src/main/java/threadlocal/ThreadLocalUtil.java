package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class ThreadLocalUtil {
    static boolean myValueFinalized;
    static boolean threadLocalUserFinalized;
    static boolean threadLocalExtensionFinalized;
    private static boolean myThreadFinalized;

    public static void setUp() {
        myValueFinalized = false;
        threadLocalUserFinalized = false;
        threadLocalExtensionFinalized = false;
        myThreadFinalized = false;
    }

    public static void setMyValueFinalized() {
        myValueFinalized = true;
    }

    public static void setThreadLocalUserFinalized() {
        threadLocalUserFinalized = true;
    }

    public static void setMyThreadLocalFinalized() {
        threadLocalExtensionFinalized = true;
    }

    public static void setMyThreadFinalized() {
        myThreadFinalized = true;
    }
}
