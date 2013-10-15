package pl.agh.student.mizmuda.lab1;

public class Semaphore {
    private volatile int value;

    public Semaphore(int value) {
        this.value = value;
    }

    public synchronized void V() {
        this.value++;
        if (value == 1) {
            notify();
        }
    }

    public synchronized void P() throws InterruptedException {
        if (value == 0) {
            wait();
        }
        this.value--;
    }

}
