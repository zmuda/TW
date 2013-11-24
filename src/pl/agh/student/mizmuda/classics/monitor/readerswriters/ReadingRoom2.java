package pl.agh.student.mizmuda.classics.monitor.readerswriters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReadingRoom2 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition readers = lock.newCondition();
    private Condition writers = lock.newCondition();
    private Condition firstWriter = lock.newCondition();
    private int activeReaders = 0;
    private boolean activeWriter = false;

    public void startReading() throws InterruptedException {
        lock.lock();
        try {
            while (activeWriter || lock.hasWaiters(firstWriter)) {
                readers.await();
            }
            activeReaders++;
        } finally {
            lock.unlock();
        }
    }

    public void finishReading() {
        lock.lock();
        activeReaders--;
        if (activeReaders == 0) {
            if (lock.hasWaiters(firstWriter)) {
                firstWriter.signal();
            } else {
                writers.signal();
            }
        }
        lock.unlock();
    }

    public void startWriting() throws InterruptedException {
        lock.lock();
        try {
            //dopóki wyróżniony pisarz nie zacznie i nie skończy - czekamy osobno
            while (activeReaders > 0 || lock.hasWaiters(firstWriter)) {
                writers.await();
            }
            while (activeReaders > 0) {
                firstWriter.await();
            }
            activeWriter = true;
        } finally {
            lock.unlock();
        }
    }

    public void finishWriting() {
        lock.lock();
        activeWriter = false;
        if (lock.hasWaiters(readers)) {
            readers.signalAll();
        } else {
            //jesteśmy wyróżnionym do końca pracy - nikt nie czeka na firstWriter
            writers.signal();
        }
        lock.unlock();
    }

}
