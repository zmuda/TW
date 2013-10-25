package pl.agh.student.mizmuda.lab2.zad1;

import org.apache.log4j.Logger;

public class Consumer implements Runnable {
    private Logger logger = Logger.getLogger("lab2.zad1");
    private final Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            buffer.popElement();
            logger.info("\tconsumed element");
        }
    }
}
