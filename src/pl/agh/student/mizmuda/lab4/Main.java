package pl.agh.student.mizmuda.lab4;

import org.apache.log4j.BasicConfigurator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        int prods = 30;
        int cons = 30;
        int M = 40;
        IBuffer<Integer> buffer = new Buffer<Integer>(2 * M);
        ExecutorService service = Executors.newFixedThreadPool(prods + cons);
        for (int i = 0; i < prods; i++) {
            service.submit(new Producer<Integer>(buffer, 1));
        }
        for (int i = 0; i < cons; i++) {
            service.submit(new Consumer<Integer>(buffer));
        }
    }
}
