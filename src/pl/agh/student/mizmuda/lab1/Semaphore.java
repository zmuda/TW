package pl.agh.student.mizmuda.lab1;

public class Semaphore {
    private volatile int value;

    public Semaphore(int value) {
        this.value = value;
    }

    public synchronized void V() {

        this.value++;
        notify();

    }

    public synchronized void P() {
        while (value == 0) {

            try {
                wait();
            } catch (InterruptedException e) {
                //om nom nom - exception eaten
            }
        }
        this.value--;

    }

}
