package pl.agh.student.mizmuda.lab2.zad1;

import org.apache.log4j.Logger;

public class Producer implements Runnable {
    private Logger logger = Logger.getLogger("lab2.zad1");
    private final Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            buffer.pushElement(new Integer(1));
            logger.info("produced element");
        }
    }
}
