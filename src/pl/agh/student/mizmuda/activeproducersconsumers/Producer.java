package pl.agh.student.mizmuda.activeproducersconsumers;

import pl.agh.student.mizmuda.activeproducersconsumers.problemspecific.ProducersConsumersService;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer implements Runnable {

    private ProducersConsumersService<Integer> service;
    private Random random;
    private int bufferLimit;

    public Producer(ProducersConsumersService<Integer> service, Random random, int bufferLimit) {
        this.service = service;
        this.random = random;
        this.bufferLimit = bufferLimit;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Future<Integer> future = service.produce(random.nextInt(bufferLimit) / 2);
                future.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
