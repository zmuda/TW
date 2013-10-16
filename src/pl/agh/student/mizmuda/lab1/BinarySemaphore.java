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
        if (awaits > 0) {
            notify();
        } else {
            this.opened = true;
        }
    }

    public synchronized void P(){
        if (!opened) {
            awaits++;
            try {
                wait();
            } catch (InterruptedException e) {
                //om nom nom - exception eaten
            } finally {
                awaits--;
            }
        }
        this.opened = false;
    }

}
