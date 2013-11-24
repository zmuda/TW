package pl.agh.student.mizmuda.classics.monitor.readerswriters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReadingRoom {
    private ReentrantLock lock = new ReentrantLock();
    private Condition readers = lock.newCondition();
    private Condition writers = lock.newCondition();
    private int activeReaders = 0;
    private boolean activeWriter = false;
    private boolean lettingTrough = false;
    private boolean readerWhoLetWriterResumed = false;
    private boolean gotTrough = false;

    public void startReading() throws InterruptedException {
        lock.lock();
        try {
            //przepuszczamy JEDNEGO pisarza Z KOLEJKI aby nie zagładzać pisarzy
            //jeżeli czytelnik przepuszcza kogoś inny nie może przepuszczać kolejnego
            //dopóki poprzedni (albo jakikolwiek inny) nie wznowi pracy (zakończy przepuszczać)
            if (!activeWriter && lock.hasWaiters(writers) && !lettingTrough && readerWhoLetWriterResumed) {
                lettingTrough = true;
                readerWhoLetWriterResumed = false;
            }
            //po ewentualnym przepuszczeniu, czekamy aż czytanie się zakończy
            //czekamy też, jeżeli przepuszczany nie skończył pisać
            while (activeWriter || lettingTrough) {
                readers.await();
            }
            readerWhoLetWriterResumed = true;
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
        } finally {
            lock.unlock();
        }
    }

    public void finishWriting() {
        lock.lock();
        lettingTrough = false;
        activeWriter = false;
        if (lock.hasWaiters(readers)) {
            readers.signalAll();
        } else {
            writers.signal();
        }
        lock.unlock();
    }

}
