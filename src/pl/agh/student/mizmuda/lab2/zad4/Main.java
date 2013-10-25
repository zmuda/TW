package pl.agh.student.mizmuda.lab2.zad4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        int prods = 3;
        int cons = 3;
        Buffer buffer = new Buffer(1);
        ExecutorService service = Executors.newFixedThreadPool(prods + cons);
        for (int i = 0; i < prods; i++) {
            service.submit(new Producer(buffer));
        }
        for (int i = 0; i < cons; i++) {
            service.submit(new Consumer(buffer));
        }
    }
}
