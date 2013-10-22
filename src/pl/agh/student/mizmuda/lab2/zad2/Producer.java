package pl.agh.student.mizmuda.lab2.zad2;

public class Producer implements Runnable {
    private final Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            buffer.pushElement(new Integer(1));
        }
    }
}
