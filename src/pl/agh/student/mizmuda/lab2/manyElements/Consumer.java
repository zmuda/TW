package pl.agh.student.mizmuda.lab2.manyElements;


import java.util.Random;

public class Consumer implements Runnable {
    private final Buffer buffer;
    private final int M;
    private final Random random = new Random(System.currentTimeMillis());

    public Consumer(Buffer buffer, int M) {
        this.buffer = buffer;
        this.M = M;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(12);
            } catch (InterruptedException e) {
            }
            buffer.popElements(random.nextInt(M) + 1);
        }
    }
}
