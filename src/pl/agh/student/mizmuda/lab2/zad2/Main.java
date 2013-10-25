package pl.agh.student.mizmuda.lab2.zad2;

import org.apache.log4j.BasicConfigurator;
import pl.agh.student.mizmuda.lab2.common.Buffer;
import pl.agh.student.mizmuda.lab2.common.BufferMonitor;
import pl.agh.student.mizmuda.lab2.common.Consumer;
import pl.agh.student.mizmuda.lab2.common.Producer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        Buffer buffer = new BufferMonitor(4, "zad2");
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Producer(buffer));
        service.submit(new Consumer(buffer));
    }
}
