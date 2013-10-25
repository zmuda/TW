package pl.agh.student.mizmuda.lab2.common;


import java.util.Random;

public class Consumer implements Runnable {
    private final Buffer buffer;
    private final Random random = new Random(System.currentTimeMillis());

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
            }
            buffer.poolElement();
        }
    }
}
