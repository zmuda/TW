package pl.agh.student.mizmuda.lab2.asynchronous.v2;


import java.util.Random;

public class Producer<T> implements Runnable {
    private final Buffer<T> buffer;
    private final T exampleInstance;
    private final Random random = new Random(System.currentTimeMillis());

    public Producer(Buffer buffer, T exampleInstance) {
        this.buffer = buffer;
        this.exampleInstance = exampleInstance;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(random.nextInt(100) + 1);
            } catch (InterruptedException e) {
            }
            buffer.pushElement(exampleInstance);
        }
    }
}
