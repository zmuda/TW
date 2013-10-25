package pl.agh.student.mizmuda.lab2.zad4;

public class Consumer implements Runnable {
    private final Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            buffer.popElement();
        }
    }
}
