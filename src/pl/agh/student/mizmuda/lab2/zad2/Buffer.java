package pl.agh.student.mizmuda.lab2.zad2;

import org.apache.log4j.Logger;

import java.util.LinkedList;

public class Buffer {
    private int limit = 4;
    private Logger logger = Logger.getLogger("lab2.zad2");
    private LinkedList<Integer> data = new LinkedList<Integer>();

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
        logger.info("\treturned element " + getString());
        return data.poll();
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
