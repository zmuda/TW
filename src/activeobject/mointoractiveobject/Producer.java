package activeobject.mointoractiveobject;


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
                int address = buffer.acquireEmpty();
                Thread.sleep(random.nextInt(100));
                buffer.finalizeFilling(address);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
