package pl.agh.student.mizmuda.lab2.common;

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BufferLock implements Buffer {
    private final int limit;
    private Logger logger;
    ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
    private LinkedList<Integer> data = new LinkedList<Integer>();

    public BufferLock(int limit, String loggerRef) {
        this.limit = limit;
        logger = Logger.getLogger(loggerRef);
    }

    public void pushElement(Integer element) {
        lock.lock();
        while (data.size() == limit) {
            try {
                notFull.await();
            } catch (InterruptedException e) {
            }
        }
        data.push(element);
        logger.info("added element\t\t" + getString());
        notEmpty.signal();
        lock.unlock();
    }

    public Integer poolElement() {
        lock.lock();
        while (data.isEmpty()) {
            try {
                notEmpty.await();
            } catch (InterruptedException e) {
            }
        }
        Integer tmp = data.poll();
        logger.info("\treturned element " + getString());
        notFull.signal();
        lock.unlock();
        return tmp;
    }

    public String getString() {
        StringBuffer res = new StringBuffer("[");
        for (Integer i : data) {
            res.append((i == null) ? "□" : "■");
        }
        res.append(("]"));
        return res.toString();
    }
}
