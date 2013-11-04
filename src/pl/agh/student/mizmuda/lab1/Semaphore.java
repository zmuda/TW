package pl.agh.student.mizmuda.lab1;

public class Semaphore {
    private volatile int value;


    public Semaphore(int value) {
        this.value = value;
    }

    public synchronized void V() {
        value++;
        notify();
    }

    public synchronized void P() throws InterruptedException {
        while (value == 0) {
            wait();
        }
        value--;
    }

}
