package pl.agh.student.mizmuda.activeobject;


import java.util.Collection;
import java.util.Random;

public class Consumer implements Runnable {
    private final IBuffer buffer;
    private final Random random = new Random(System.currentTimeMillis());

    public Consumer(IBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                int i = random.nextInt(buffer.getMaxSize() / 2);
                Collection<Integer> addresses = buffer.acquireElements(i);
                Thread.sleep(random.nextInt(100));
                buffer.releaseElements(addresses);
                Thread.sleep(random.nextInt(10));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
