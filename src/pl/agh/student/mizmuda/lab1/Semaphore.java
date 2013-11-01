package pl.agh.student.mizmuda.lab1;

public class Semaphore {
    private volatile int value;
    private volatile int toWake = 0;
    private volatile int awaitingForWake = 0;
    private volatile int toLetIn = 0;


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
