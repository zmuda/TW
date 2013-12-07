package activeobject.activeproducersconsumers;


import activeobject.activeproducersconsumers.problemspecific.ProducersConsumersService;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Consumer implements Runnable {
    private ProducersConsumersService<Integer> service;
    private Random random;
    private int bufferLimit;

    public Consumer(ProducersConsumersService<Integer> service, Random random, int bufferLimit) {
        this.service = service;
        this.random = random;
        this.bufferLimit = bufferLimit;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Future<Integer> future = service.consume(1);
                future.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
