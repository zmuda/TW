package pl.agh.student.mizmuda.lab1;

public class BinarySemaphore {
    private volatile boolean opened;

    public BinarySemaphore(boolean opened) {
        this.opened = opened;
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
