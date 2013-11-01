package pl.agh.student.mizmuda.lab3;


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
                int address = buffer.acquireFull();
                Thread.sleep(random.nextInt(1000));
                buffer.finalizeEmptying(address);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
