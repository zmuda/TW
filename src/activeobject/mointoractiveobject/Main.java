package activeobject.mointoractiveobject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static int producers = 5;
    private static int consumers = 5;
    private static int bufferSize = 12;


    public static void main(String[] args) {
        IBuffer buffer = new Buffer(bufferSize);
        ExecutorService service = Executors.newFixedThreadPool(producers + consumers);
        for (int i = 0; i < producers; i++) {
            service.submit(new Producer(buffer));
        }
        for (int i = 0; i < consumers; i++) {
            service.submit(new Consumer(buffer));
        }
    }
}
