package pl.agh.student.mizmuda.lab2.zad4;

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final int limit;
    private Logger logger = Logger.getLogger("lab2.zad4");
    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
    private LinkedList<Integer> data = new LinkedList<Integer>();

    public Buffer(int limit) {
        this.limit = limit;
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

    public Integer popElement() {
        lock.lock();
        while (data.isEmpty()) {
            try {
                notEmpty.await();
            } catch (InterruptedException e) {
            }
        }
        logger.info("\treturned element " + getString());
        Integer tmp = data.poll();
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
