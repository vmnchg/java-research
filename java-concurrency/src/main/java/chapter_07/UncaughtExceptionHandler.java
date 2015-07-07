package chapter_07;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import net.jcip.UEHLogger;


public class UncaughtExceptionHandler {
    final int       NTHREADS = 20;
    ExecutorService exec     = Executors.newFixedThreadPool(NTHREADS, (ThreadFactory) new UEHLogger());

    public static void main(String[] args) {
        UncaughtExceptionHandler uce = new UncaughtExceptionHandler();
        uce.m1();
    }

    private void m1() {
        // TODO Auto-generated method stub
    }
}
