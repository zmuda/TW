package activeobject.activeproducersconsumers.core;


import activeobject.TaskDuration;
import org.apache.log4j.Logger;

import java.util.Queue;
import java.util.Random;

public class ConsumeRequest<T> implements IMethodRequest<T> {
    private Logger logger = Logger.getLogger("activeobject - losowa ilosc");
    private Queue<T> buffer;
    private int howMany;
    private Random random;

    public ConsumeRequest(Queue<T> buffer, int howMany, Random random) {
        this.buffer = buffer;
        this.howMany = howMany;
        this.random = random;
    }

    @Override
    public boolean guard() {
        return buffer.size() >= howMany;
    }

    @Override
    public T execute() throws InterruptedException {
        logger.info("consumes: " + howMany);
        T ret = null;
        for (int i = 0; i < howMany; i++) {
            ret = buffer.poll();
            TaskDuration.waitForItemToConsume();
        }
        logger.info("Consumed: " + howMany);
        logger.info("\t>>>: " + buffer.size());
        return ret;//we return last consumed - why not? (null is good as well)
    }

    @Override
    public void eventually() {
    }
}
