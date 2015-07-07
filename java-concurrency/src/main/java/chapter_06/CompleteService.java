package chapter_06;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CompleteService {
    int                        NTHREADS = 10;
    ExecutorService            exec     = Executors.newFixedThreadPool(NTHREADS);
    CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(exec);
    Logger logger = Logger.getLogger(CompleteService.class.getName());

    public void sampleCompleteService(int runs) {
        for (int i = runs; i > 0; i--) {
            Counting<Integer> c = new Counting<Integer>(i);
            Future<Integer> ft = completionService.submit(c);
            System.out.printf("ft submit %10s \n", ft);

            // try {
            // System.out.printf("%10s ", ft.get());
            // } catch (InterruptedException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // } catch (ExecutionException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }
        try {
            Thread.currentThread().sleep(TimeUnit.SECONDS.toSeconds(5000));
            System.out.println("just finished sleeping");
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            for (int i = runs; i > 0; i--) {
                System.out.printf("- calling  completionService.take(). ft take, i %2d\n", i);
                Future<Integer> ft = completionService.take();
                System.out.printf("+ received completionService.take(). ft take, i %2d get %2d\n", i, ft.get());
            }
            exec.shutdown();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}

class Counting<T> implements Callable<T> {
    int input = 0;

    public Counting(int input) {
        this.input = input;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T call() {
        for (int i = 0; i < input; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.printf("call %s, [%2d < %2d]\n", Thread.currentThread(), i, input);
        }
        System.out.printf("Thread finishing [%2d] \n", input);
        return (T) new Integer(input);
    }
}