package pl.agh.student.mizmuda.lab2.multimonitor;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DuplexMonitor {
    private Logger logger = Logger.getLogger("duplexMonitor");
    private ReentrantLock lock = new ReentrantLock();
    private int waitingForA = 0;
    private int waitingForB = 0;
    private int waitingForAnB = 0;
    private Condition justA = lock.newCondition();
    private Condition justB = lock.newCondition();
    private Condition partA = lock.newCondition();
    private Condition partB = lock.newCondition();
    private final int limitA;
    private final int limitB;
    private ConcurrentLinkedQueue<Integer> dataA = new ConcurrentLinkedQueue<Integer>();
    private ConcurrentLinkedQueue<Integer> dataB = new ConcurrentLinkedQueue<Integer>();

    public DuplexMonitor(int limitA, int limitB) {
        this.limitA = limitA;
        this.limitB = limitB;
        for (int i = 0; i < limitA; i++) {
            dataA.add(-i);
        }
        for (int i = 0; i < limitB; i++) {
            dataB.add(i);
        }
    }

    public Integer lockA() {
        lock.lock();
        while (dataA.size() == 0) {
            waitingForA++;
            try {
                justA.await();
            } catch (InterruptedException e) {
            } finally {
                waitingForA--;
            }
        }
        logger.info("locked A");
        Integer t = dataA.poll();
        lock.unlock();
        return t;
    }

    public Integer lockB() {
        lock.lock();
        while (dataB.size() == 0) {
            waitingForB++;
            try {
                justB.await();
            } catch (InterruptedException e) {
            } finally {
                waitingForB--;
            }
        }
        logger.info("locked B");
        Integer poll = dataB.poll();
        lock.unlock();
        return poll;
    }

    public ArrayList<Integer> lockAnB() {
        lock.lock();
        Integer reservedA = null;
        Integer reservedB = null;
        while (((dataA.size() == 0) && reservedA == null) || ((dataB.size() == 0) && reservedB == null)) {
            waitingForAnB++;
            try {
                if ((dataA.size() == 0) && reservedA == null) {
                    partA.await();
                    reservedA = dataA.poll();
                }
                if ((dataB.size() == 0) && reservedB == null) {
                    partB.await();
                    reservedB = dataB.poll();
                }
            } catch (InterruptedException e) {
            } finally {
                waitingForAnB--;
            }
        }
        if (reservedA == null) {
            reservedA = dataA.poll();
            reservedB = dataB.poll();
        }
        ArrayList<Integer> ret = new ArrayList<Integer>();
        ret.add(reservedA);
        ret.add(reservedB);
        logger.info("locked A and B");
        lock.unlock();
        return ret;
    }

    public void unlockA(Integer a) {
        lock.lock();
        dataA.add(a);
        if (waitingForAnB > 0) {
            partA.signal();
        } else {
            justA.signal();
        }
        logger.info("unlocked A");
        lock.unlock();
    }

    public void unlockB(Integer b) {
        lock.lock();
        dataB.add(b);
        if (waitingForAnB > 0) {
            partB.signal();
        } else {
            justB.signal();
        }
        logger.info("unlocked B");
        lock.unlock();
    }

    public void unlockPartA(Integer a) {
        lock.lock();
        dataA.add(a);
        if (waitingForA > 0) {
            justA.signal();
        } else {
            partA.signal();
        }
        logger.info("unlocked part A");
        lock.unlock();
    }

    public void unlockPartB(Integer b) {
        lock.lock();
        dataB.add(b);
        if (waitingForB > 0) {
            justB.signal();
        } else {
            partB.signal();
        }
        logger.info("unlocked part B");
        lock.unlock();
    }
}
