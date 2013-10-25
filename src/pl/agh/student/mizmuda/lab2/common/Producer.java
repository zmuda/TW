package pl.agh.student.mizmuda.lab2.common;

public class Producer implements Runnable {
    private final Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(12);
            } catch (InterruptedException e) {
            }
            buffer.pushElement(new Integer(1));
        }
    }
}
