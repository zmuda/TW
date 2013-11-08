package pl.agh.student.mizmuda.lab4;


import java.util.LinkedList;
import java.util.Random;

public class Producer<T> implements Runnable {
    private final IBuffer<T> buffer;
    private final T productInstance;
    private final Random random = new Random(System.currentTimeMillis());

    public Producer(IBuffer<T> buffer, T product) {
        this.buffer = buffer;
        this.productInstance = product;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                LinkedList<T> list = new LinkedList<T>();
                int i = random.nextInt(buffer.getMaxSize() / 2);
                while (i-- > 0) {
                    list.add(productInstance);
                }
                buffer.push(list);
                Thread.sleep(random.nextInt(100));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
