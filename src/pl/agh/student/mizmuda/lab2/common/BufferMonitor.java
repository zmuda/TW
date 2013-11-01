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

    public synchronized void pushElement(Integer element) throws InterruptedException {
        while (data.size() == limit) {
            wait();
        }
        data.push(element);
        notify();
        logger.info("added element\t\t" + getString());
    }

    public synchronized Integer poolElement() throws InterruptedException {
        while (data.isEmpty()) {
            wait();
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
