package pl.agh.student.mizmuda.lab1prep;

public class SyncSemaphore implements Semaphore {
    private volatile int value;

    public SyncSemaphore(int value) {
        this.value = value;
    }

    @Override
    public synchronized void V() {
        this.value++;
        if (value == 1) {
            notify();
        }
    }

    @Override
    public synchronized void P(){
        if (value == 0) {
        try {
            wait();
        } catch (InterruptedException e) {
            System.err.println("P interrupted but resumed");
        }
    }
        this.value--;
    }

}
