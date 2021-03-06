package threadlocal;

/**
 * Created by vchhieng on 16/06/2015.
 */
public class ThreadLocalUtil {
    boolean myObjectFinalized;
    int myObjectFinalizedCounter = 0;

    boolean threadLocalUserFinalized;
    int threadLocalUserFinalizedCounter = 0;

    boolean threadLocalExtensionFinalized;
    int threadLocalExtensionFinalizedCounter = 0;

    boolean threadExtensionFinalized;
    int threadExtensionFinalizedCounter = 0;

    public ThreadLocalUtil() {
        myObjectFinalized = false;
        myObjectFinalizedCounter = 0;
        threadLocalUserFinalized = false;
        threadLocalUserFinalizedCounter = 0;
        threadLocalExtensionFinalized = false;
        threadLocalExtensionFinalizedCounter = 0;
        threadExtensionFinalized = false;
        threadExtensionFinalizedCounter = 0;
    }

    public synchronized void setMyValueFinalized() {
        myObjectFinalizedCounter--;
        if (myObjectFinalizedCounter == 0)    {
            myObjectFinalized = true;
        }
    }
    public synchronized void printMyValueFinalized(String methodName) {
        System.out.println(methodName + " Remaining myObjectFinalizedCounter " + myObjectFinalizedCounter);

    }
    public synchronized void setThreadLocalUserFinalized() {
        threadLocalUserFinalizedCounter--;
        if (threadLocalUserFinalizedCounter == 0) {
            threadLocalUserFinalized = true;
        }
    }
    public synchronized void printThreadLocalUserFinalized(String methodName) {
        System.out.println(methodName + " Remaining threadLocalUserFinalizedCounter " + threadLocalUserFinalizedCounter);
    }
    public synchronized void setThreadLocalExtensionFinalized() {
        threadLocalExtensionFinalizedCounter--;
        if (threadLocalExtensionFinalizedCounter == 0) {
            threadLocalExtensionFinalized = true;
        }
    }

    public synchronized void printThreadLocalExtensionFinalized(String methodName) {
        System.out.println(methodName + " Remaining threadLocalExtensionFinalizedCounter " + threadLocalExtensionFinalizedCounter);
    }

    public synchronized void setThreadExtensionFinalized() {
        threadExtensionFinalizedCounter--;
        if (threadExtensionFinalizedCounter == 0) {
            threadExtensionFinalized = true;
        }
    }

    public synchronized void printThreadExtensionFinalized(String methodName) {
        System.out.println(methodName + " Remaining threadExtensionFinalizedCounter " + threadExtensionFinalizedCounter);
    }
}
