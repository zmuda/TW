package pl.agh.student.mizmuda.lab1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockSemaphore implements Semaphore {
    private volatile int value;
    private ReentrantLock lock = new ReentrantLock();
    private Condition waiting = lock.newCondition();

    public LockSemaphore(int value) {
        this.value = value;
    }

    @Override
    public void V() {
        lock.lock();
        value++;
        if (value == 1) {
            waiting.signal();
        }
        lock.unlock();
    }

    @Override
    public void P(){
        lock.lock();
        while (value == 0) {
            try {
                waiting.await();
            } catch (InterruptedException e) {
                System.err.println("P interrupted but resumed");
            }
        }

        value--;
        lock.unlock();
    }

}
