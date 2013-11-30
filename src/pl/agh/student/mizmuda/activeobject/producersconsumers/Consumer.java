package pl.agh.student.mizmuda.activeobject.producersconsumers;


import pl.agh.student.mizmuda.activeobject.core.ISubmittableExecutor;

import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Consumer implements Runnable {
    private final ISubmittableExecutor executor;
    private final Queue<Integer> buffer;

    public Consumer(ISubmittableExecutor executor, Queue<Integer> buffer) {
        this.executor = executor;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Future<Integer> future = executor.submit(new ConsumeSubmittable(buffer));
                future.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
