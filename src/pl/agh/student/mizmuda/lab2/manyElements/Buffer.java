package pl.agh.student.mizmuda.lab2.manyElements;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final int limit;
    private Logger logger;
    ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
    private LinkedList<Integer> data = new LinkedList<Integer>();

    public Buffer(int limit, String loggerRef) {
        this.limit = limit;
        logger = Logger.getLogger(loggerRef);
    }

    public void pushElements(Collection<Integer> elements) {
        lock.lock();
        int size = elements.size();
        while (data.size() > limit - size) {
            try {
                notFull.await();
            } catch (InterruptedException e) {
            }
        }
        data.addAll(elements);
        logger.info("added " + size + " elements\t\t" + getString());
        notEmpty.signal();
        lock.unlock();
    }

    public Collection<Integer> popElements(int howMany) {
        lock.lock();
        while (data.size() < howMany) {
            try {
                notEmpty.await();
            } catch (InterruptedException e) {
            }
        }
        LinkedList<Integer> tmp = new LinkedList<Integer>();
        for (int i = 0; i < howMany; i++) {
            tmp.push(data.poll());
        }
        logger.info("\treturned " + howMany + " elements " + getString());
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
