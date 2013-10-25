package pl.agh.student.mizmuda.lab2.zad3;

import org.apache.log4j.BasicConfigurator;
import pl.agh.student.mizmuda.lab2.common.Buffer;
import pl.agh.student.mizmuda.lab2.common.BufferLock;
import pl.agh.student.mizmuda.lab2.common.Consumer;
import pl.agh.student.mizmuda.lab2.common.Producer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        int prods = 3;
        int cons = 3;
        Buffer buffer = new BufferLock(4, "zad3");
        ExecutorService service = Executors.newFixedThreadPool(prods + cons);
        for (int i = 0; i < prods; i++) {
            service.submit(new Producer(buffer));
        }
        for (int i = 0; i < cons; i++) {
            service.submit(new Consumer(buffer));
        }
    }
}
