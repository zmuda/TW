package pl.agh.student.mizmuda.lab2.zad3;

import pl.agh.student.mizmuda.lab2.zad2.Buffer;
import pl.agh.student.mizmuda.lab2.zad2.Consumer;
import pl.agh.student.mizmuda.lab2.zad2.Producer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Producer(buffer));
        service.submit(new Producer(buffer));
        service.submit(new Producer(buffer));
        service.submit(new Consumer(buffer));
        service.submit(new Consumer(buffer));
        service.submit(new Consumer(buffer));
    }
}
