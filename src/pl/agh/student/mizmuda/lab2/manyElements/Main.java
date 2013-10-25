package pl.agh.student.mizmuda.lab2.manyElements;

import org.apache.log4j.BasicConfigurator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        int prods = 3;
        int cons = 3;
        int M = 4;
        Buffer buffer = new Buffer(2 * M, "manyElements");
        ExecutorService service = Executors.newFixedThreadPool(prods + cons);
        for (int i = 0; i < prods; i++) {
            service.submit(new Producer(buffer, M));
        }
        for (int i = 0; i < cons; i++) {
            service.submit(new Consumer(buffer, M));
        }
    }
}
