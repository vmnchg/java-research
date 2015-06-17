package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class ThreadLocalUtil {
    static boolean myValueFinalized;
    static int myValueFinalizedCounter = 0;
    static boolean threadLocalUserFinalized;
    static int threadLocalUserFinalizedCounter = 0;
    static boolean threadLocalExtensionFinalized;
    static int threadLocalExtensionFinalizedCounter = 0;


    public static void setUp() {
        myValueFinalized = false;
        threadLocalUserFinalized = false;
        threadLocalExtensionFinalized = false;
    }

    public static synchronized void setMyValueFinalized() {
        myValueFinalizedCounter--;
        if (myValueFinalizedCounter == 0)    {
            myValueFinalized = true;
        }
    }
    public static void printMyValueFinalized() {
        System.out.println("Remaining myValueFinalizedCounter " + myValueFinalizedCounter);

    }
    public static void setThreadLocalUserFinalized() {
        threadLocalUserFinalizedCounter--;
        if (threadLocalUserFinalizedCounter == 0) {
            threadLocalUserFinalized = true;
        }
    }
    public static void printThreadLocalUserFinalized() {
        System.out.println("Remaining threadLocalUserFinalizedCounter " + threadLocalUserFinalizedCounter);
    }
    public static void setMyThreadLocalFinalized() {
        threadLocalExtensionFinalizedCounter--;
        if (threadLocalExtensionFinalizedCounter == 0) {
            threadLocalExtensionFinalized = true;
        }
    }

    public static void printMyThreadLocalFinalized() {
        System.out.println("Remaining threadLocalExtensionFinalizedCounter " + threadLocalExtensionFinalizedCounter);
    }


}
