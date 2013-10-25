package pl.agh.student.mizmuda.lab2.manyElements;


import java.util.LinkedList;
import java.util.Random;

public class Producer implements Runnable {
    private final Buffer buffer;
    private final int M;
    private final Random random = new Random(System.currentTimeMillis());

    public Producer(Buffer buffer, int m) {
        this.buffer = buffer;
        this.M = m;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(12);
            } catch (InterruptedException e) {
            }
            int portionSize = random.nextInt(M) + 1;
            LinkedList<Integer> portion = new LinkedList<Integer>();
            for (int i = 0; i < portionSize; i++) {
                portion.add(new Integer(0));
            }
            buffer.pushElements(portion);
        }
    }
}
