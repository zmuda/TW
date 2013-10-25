package pl.agh.student.mizmuda.lab2.asynchronous;

import java.util.Random;

public class Producer implements Runnable {
    private final Buffer buffer;
    private final Random random = new Random(System.currentTimeMillis());

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
            }

        }
    }
}
