package pl.agh.student.mizmuda.lab2.asynchronous.v1;

import org.apache.log4j.BasicConfigurator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        int prods = 3;
        Buffer<Integer> buffer = new Buffer<Integer>(5, "asynchronous");
        ExecutorService service = Executors.newFixedThreadPool(prods + 1);
        for (int i = 0; i < prods; i++) {
            service.submit(new Producer<Integer>(buffer, 1));
        }
        service.submit(new Consumer(buffer));
    }
}
