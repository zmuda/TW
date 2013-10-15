package pl.agh.student.mizmuda.lab1;

public class BinarySemaphore {
    private volatile boolean value;

    public BinarySemaphore(boolean value) {
        this.value = value;
    }

    public synchronized void V() {
        this.value = true;
        if (value) {
            notify();
        }
    }

    public synchronized void P() throws InterruptedException {
        if (!value) {
            wait();
        }
        this.value = false;
    }

}
