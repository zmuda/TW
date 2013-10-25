package pl.agh.student.mizmuda.lab2.multimonitor;

import org.apache.log4j.Logger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DuplexMonitor {
    private Logger logger = Logger.getLogger("duplexMonitor");
    private ReentrantLock lock = new ReentrantLock();
    private int waitingForA = 0;
    private int waitingForB = 0;
    private int waitingForAnB = 0;
    private boolean lockedA = false;
    private boolean lockedB = false;
    private Condition justA = lock.newCondition();
    private Condition justB = lock.newCondition();
    private Condition partA = lock.newCondition();
    private Condition partB = lock.newCondition();

    public void lockA() {
        lock.lock();
        while (lockedA) {
            waitingForA++;
            try {
                justA.await();
            } catch (InterruptedException e) {
            } finally {
                waitingForA--;
            }
        }
        lockedA = true;
        logger.info("locked A");
        lock.unlock();
    }

    public void lockB() {
        lock.lock();
        while (lockedB) {
            waitingForB++;
            try {
                justB.await();
            } catch (InterruptedException e) {
            } finally {
                waitingForB--;
            }
        }
        lockedB = true;
        logger.info("locked B");
        lock.unlock();
    }

    public void lockAnB() {
        lock.lock();
        boolean reservedA = false;
        boolean reservedB = false;
        while ((lockedA && !reservedA) || (lockedB && !reservedB)) {
            waitingForAnB++;
            try {
                if (lockedA && !reservedA) {
                    partA.await();
                    lockedA = true;
                    reservedA = true;
                }
                if (lockedB && !reservedB) {
                    partB.await();
                    lockedB = true;
                    reservedB = true;
                }
            } catch (InterruptedException e) {
            } finally {
                waitingForAnB--;
            }
        }
        lockedA = true;
        lockedB = true;
        logger.info("locked A and B");
        lock.unlock();
    }

    public void unlockA() {
        lock.lock();
        lockedA = false;
        if (waitingForAnB > 0) {
            partA.signal();
        } else {
            justA.signal();
        }
        logger.info("unlocked A");
        lock.unlock();
    }

    public void unlockB() {
        lock.lock();
        lockedB = false;
        if (waitingForAnB > 0) {
            partB.signal();
        } else {
            justB.signal();
        }
        logger.info("unlocked B");
        lock.unlock();
    }

    public void unlockPartA() {
        lock.lock();
        lockedA = false;
        if (waitingForA > 0) {
            justA.signal();
        } else {
            partA.signal();
        }
        logger.info("unlocked part A");
        lock.unlock();
    }

    public void unlockPartB() {
        lock.lock();
        lockedB = false;
        if (waitingForB > 0) {
            justB.signal();
        } else {
            partB.signal();
        }
        logger.info("unlocked part B");
        lock.unlock();
    }
}
