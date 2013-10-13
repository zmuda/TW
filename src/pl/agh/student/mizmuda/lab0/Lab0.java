package pl.agh.student.mizmuda.lab0;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Lab0 {
    private static final int times = 1000000;
    private static final int threads = 100;
    private static final Value value = new Value(0);

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            service.submit(new Incrementer(value, times));
        }
        boolean terminated = false;
        service.shutdown();
        while (!terminated) {
            try {
                terminated = service.awaitTermination(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                terminated = false; //just indicating
            }
        }
        System.out.print("Threads:\t\t");
        System.out.println(threads);
        System.out.print("Incr. per thread:\t");
        System.out.println(times);
        System.out.print("Expected:\t\t");
        System.out.println(threads * times);
        System.out.print("Got:\t\t\t");
        System.out.println(value.getValue());
    }
}
