package activeobject.activeproducersconsumers.core;


import activeobject.TaskAbstractionAndStats;
import org.apache.log4j.Logger;

import java.util.Queue;

public class ProduceRequest<T> implements IMethodRequest<T> {
    private Logger logger = Logger.getLogger("activeobject - losowa ilosc");
    private Queue<T> buffer;
    private T instance;
    private int bufferLimit;

    public ProduceRequest(Queue<T> buffer, T instance, int bufferLimit) {
        this.buffer = buffer;
        this.instance = instance;
        this.bufferLimit = bufferLimit;
    }

    @Override
    public boolean guard() {
        return buffer.size() < bufferLimit;
    }

    @Override
    public T execute() throws InterruptedException {
        logger.info("produces");
        TaskAbstractionAndStats.waitForItemToProduce();
        buffer.add(instance);
        logger.info("Produced");
        logger.info("\t>>>: " + buffer.size());
        return instance;
    }
}
