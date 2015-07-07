package chapter_07;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogService {
    // If queue is full, then illegal state exception
    private BlockingQueue<String> queue        = null;
    private final LoggerThread    loggerThread = new LoggerThread();

    private boolean               isShutdown   = false;
    private int                   size         = 0;

    public LogService(int MESSAGESIZE) {
        this.queue = new LinkedBlockingQueue<String>(MESSAGESIZE);
        this.isShutdown = false;
    }

    public void stop() {
        this.loggerThread.interrupt();
        synchronized (this) {
            this.isShutdown = true;
        }
    }

    public void start() {
        loggerThread.start();
    }

    public void log(String message) throws InterruptedException {
        synchronized (this) {
            if (isShutdown)
                throw new IllegalStateException(); // throw back to client side
            size++;
        }

        queue.put(message);
    }

    class LoggerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (LogService.this) {
                        if (isShutdown && LogService.this.size == 0) {
                            break;
                        }
                    }
                    // blocking method
                    String message = queue.take();

                    synchronized (LogService.this) {
                        LogService.this.size--;
                    }

                    System.out.println("<< " + message);
                } catch (InterruptedException e) {
                    // VMC: DO NOTING HERE
                }
            }
        }
    }
}
