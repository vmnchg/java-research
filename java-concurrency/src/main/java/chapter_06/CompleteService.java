package chapter_06;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class CompleteService {
    final int numberOfThreads;
    final ExecutorService executorService;
    final CompletionService<Integer> completionService;
    final Logger logger = Logger.getLogger(CompleteService.class.getName());

    public CompleteService(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        this.executorService = Executors.newFixedThreadPool(this.numberOfThreads);
        this.completionService = new ExecutorCompletionService<Integer>(executorService);
    }

    public int sampleCompleteService(int runs) {
        int total = 0;
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
            for (int i = runs; i > 0; i--) {
                System.out.printf("- calling  completionService.take(). ft take, counter %2d\n", i);
                Future<Integer> ft = completionService.take();
                total += ft.get();
                System.out.printf("+ received completionService.take(). ft take, counter %2d tasknumber %2d\n", i, ft.get());
            }
            executorService.shutdown();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return total;
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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.printf("callable %s, [i=%2d < input=%2d]\n", Thread.currentThread(), i, input);
        }
        System.out.printf("callable finished input=[%2d] \n", input);
        return (T) new Integer(input);
    }
}