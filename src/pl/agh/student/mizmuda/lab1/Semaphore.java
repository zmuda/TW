package pl.agh.student.mizmuda.lab1;

public class Semaphore {
    private volatile int value;
    private volatile int awaits;

    public Semaphore(int value) {
        this.value = value;
    }

    public synchronized void V() {
        if (value == 0 && awaits > 0) {
            notify();
        } else {
            this.value++;
        }
    }

    public synchronized void P() throws InterruptedException {
        if (value == 0) {
            awaits++;
            try {
                wait();
            } finally {
                awaits--;
            }
        }
        this.value--;
    }

}
