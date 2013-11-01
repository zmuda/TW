package pl.agh.student.mizmuda.lab1;

public class BinarySemaphore {
    private volatile boolean opened;
    private volatile int awaits;

    public BinarySemaphore(boolean opened) {
        this.opened = opened;
        this.awaits = 0;
    }

    public synchronized void V() {
        this.opened = true;
        notify();
    }

    public synchronized void P() throws InterruptedException {
        while (!opened) {
            wait();
        }
        this.opened = false;

    }

}
