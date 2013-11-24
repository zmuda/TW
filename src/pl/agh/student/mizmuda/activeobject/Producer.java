package pl.agh.student.mizmuda.activeobject;


import java.util.Collection;
import java.util.Random;

public class Producer implements Runnable {
    private final IBuffer buffer;
    private final Random random = new Random(System.currentTimeMillis());

    public Producer(IBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                int i = random.nextInt(buffer.getMaxSize() / 2);
                Collection<Integer> addresses = buffer.acquireSpace(i);
                Thread.sleep(random.nextInt(100));
                buffer.releaseSpace(addresses);
                Thread.sleep(random.nextInt(10));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
