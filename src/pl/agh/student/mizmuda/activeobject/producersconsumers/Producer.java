package pl.agh.student.mizmuda.activeobject.producersconsumers;


import pl.agh.student.mizmuda.activeobject.core.ISubmittableExecutor;

import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer implements Runnable {
    private final ISubmittableExecutor executor;
    private final Queue<Integer> buffer;
    private final int bufferSize;

    public Producer(ISubmittableExecutor executor, Queue<Integer> buffer, int bufferSize) {
        this.executor = executor;
        this.buffer = buffer;
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Future<Integer> future = executor.submit(new ProduceSubmittable(buffer, bufferSize));
                future.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
