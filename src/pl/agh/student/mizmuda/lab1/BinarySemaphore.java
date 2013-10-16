package pl.agh.student.mizmuda.lab1;

public class BinarySemaphore {
    private volatile boolean opened;
    private volatile int awaits;

    public BinarySemaphore(boolean opened) {
        this.opened = opened;
        this.awaits = 0;
    }

    public synchronized void V() {
        if (opened) {
            throw new IllegalStateException();
        }
        this.opened = true;
        notify();

    }

    public synchronized void P() {
        while (!opened) {
            try {
                wait();
            } catch (InterruptedException e) {
                //om nom nom - exception eaten
            }
        }
        this.opened = false;

    }

}
