package pl.agh.student.mizmuda.lab5;

import org.apache.log4j.BasicConfigurator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        int prods = 5;
        int cons = 5;
        int M = 28;
        IBuffer buffer = new Buffer(2 * M);
        ExecutorService service = Executors.newFixedThreadPool(prods + cons);
        for (int i = 0; i < prods; i++) {
            service.submit(new Producer(buffer));
        }
        for (int i = 0; i < cons; i++) {
            service.submit(new Consumer(buffer));
        }
    }
}
