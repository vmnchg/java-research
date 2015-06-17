package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class ThreadLocalUtil {
    static boolean myObjectFinalized;
    static int myValueFinalizedCounter = 0;

    static boolean threadLocalUserFinalized;
    static int threadLocalUserFinalizedCounter = 0;

    static boolean threadLocalExtensionFinalized;
    static int threadLocalExtensionFinalizedCounter = 0;

    static boolean threadExtensionFinalized;
    static int threadExtensionFinalizedCounter = 0;

    public static synchronized void setUp() {
        myObjectFinalized = false;
        myValueFinalizedCounter = 0;
        threadLocalUserFinalized = false;
        threadLocalUserFinalizedCounter = 0;
        threadLocalExtensionFinalized = false;
        threadLocalExtensionFinalizedCounter = 0;
        threadExtensionFinalized = false;
        threadExtensionFinalizedCounter = 0;
    }

    public static synchronized void setMyValueFinalized() {
        myValueFinalizedCounter--;
        if (myValueFinalizedCounter == 0)    {
            myObjectFinalized = true;
        }
    }
    public static synchronized void printMyValueFinalized() {
        System.out.println("Remaining myValueFinalizedCounter " + myValueFinalizedCounter);

    }
    public static synchronized void setThreadLocalUserFinalized() {
        threadLocalUserFinalizedCounter--;
        if (threadLocalUserFinalizedCounter == 0) {
            threadLocalUserFinalized = true;
        }
    }
    public static synchronized void printThreadLocalUserFinalized() {
        System.out.println("Remaining threadLocalUserFinalizedCounter " + threadLocalUserFinalizedCounter);
    }
    public static synchronized void setThreadLocalExtensionFinalized() {
        threadLocalExtensionFinalizedCounter--;
        if (threadLocalExtensionFinalizedCounter == 0) {
            threadLocalExtensionFinalized = true;
        }
    }

    public static synchronized void printThreadLocalExtensionFinalized() {
        System.out.println("Remaining threadLocalExtensionFinalizedCounter " + threadLocalExtensionFinalizedCounter);
    }

    public static synchronized void setThreadExtensionFinalized() {
        threadExtensionFinalizedCounter--;
        if (threadExtensionFinalizedCounter == 0) {
            threadExtensionFinalized = true;
        }
    }

    public static synchronized void printThreadExtensionFinalized() {
        System.out.println("Remaining threadExtensionFinalizedCounter " + threadExtensionFinalizedCounter);
    }
}
