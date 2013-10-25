package pl.agh.student.mizmuda.lab2.common;

import org.apache.log4j.Logger;

import java.util.LinkedList;

public class BufferMonitor implements Buffer {
    private final int limit;
    private Logger logger;
    private LinkedList<Integer> data = new LinkedList<Integer>();

    public BufferMonitor(int limit, String loggerRef) {
        this.limit = limit;
        logger = Logger.getLogger(loggerRef);
    }

    public synchronized void pushElement(Integer element) {
        while (data.size() == limit) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        data.push(element);
        notify();
        logger.info("added element\t\t" + getString());
    }

    public synchronized Integer popElement() {
        while (data.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        notify();
        Integer tmp = data.poll();
        logger.info("\treturned element " + getString());
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
