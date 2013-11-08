package pl.agh.student.mizmuda.lab4;


import java.util.Random;

public class Consumer<T> implements Runnable {
    private final IBuffer<T> buffer;
    private final Random random = new Random(System.currentTimeMillis());

    public Consumer(IBuffer<T> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                buffer.pool(random.nextInt(buffer.getMaxSize()) / 2);
                Thread.sleep(random.nextInt(100));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
