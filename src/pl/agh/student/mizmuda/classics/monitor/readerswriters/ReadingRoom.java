package pl.agh.student.mizmuda.classics.monitor.readerswriters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReadingRoom {
    private ReentrantLock lock = new ReentrantLock();
    private Condition readers = lock.newCondition();
    private Condition writers = lock.newCondition();
    private int activeReaders = 0;
    private boolean activeWriter = false;
    private boolean readersDoNotJoin = false;

    public void startReading() throws InterruptedException {
        lock.lock();
        try {
            //przepuszczamy jednego pisarza Z KOLEJKI aby nie zagładzać pisarzy
            if (!activeWriter && lock.hasWaiters(writers)) {
                readersDoNotJoin = true;
            }
            //po ewentualnym przepuszczeniu, czekamy aż czytanie się zakończy
            //czekamy też, jeżeli przepuszczany nie zaczął pisać
            while (activeWriter || readersDoNotJoin) {
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
            writers.signal();
        }
        lock.unlock();
    }

    public void startWriting() throws InterruptedException {
        lock.lock();
        try {
            while (activeReaders > 0) {
                writers.await();
            }
            activeWriter = true;
            //zaczęliśmy pisanie - nie musimy wstrzymywać czytelników, którzy nas przepuszczają
            readersDoNotJoin = false;
        } finally {
            lock.unlock();
        }
    }

    public void finishWriting() {
        lock.lock();
        activeWriter = false;
        readers.signalAll();
        lock.unlock();
    }

}
