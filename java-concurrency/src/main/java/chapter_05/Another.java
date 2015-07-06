package chapter_05;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Another {
    FutureTask<PassAble> ft = new FutureTask<PassAble>(new PassAble());
    Lock lock = new ReentrantLock();
    Semaphore s = new Semaphore(10);
    CyclicBarrier cb = new CyclicBarrier(1);
    BlockingQueue bq = new ArrayBlockingQueue<Object>(1);

    public Another() {
        lock = new ReentrantLock();
    }
}

class PassAble implements Callable {
    @Override
    public Object call() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}