package activeobject.activeproducersconsumers.core;


import activeobject.TaskAbstractionAndStats;
import org.apache.log4j.Logger;

import java.util.Queue;
import java.util.Random;

public class ProduceRequest<T> implements IMethodRequest<T> {
    private Logger logger = Logger.getLogger("activeobject - losowa ilosc");
    private Queue<T> buffer;
    private int howMany;
    private T instance;
    private int bufferLimit;
    private Random random;

    public ProduceRequest(Queue<T> buffer, int howMany, T instance, int bufferLimit, Random random) {
        this.buffer = buffer;
        this.howMany = howMany;
        this.instance = instance;
        this.bufferLimit = bufferLimit;
        this.random = random;
    }

    @Override
    public boolean guard() {
        return (buffer.size() + howMany) <= bufferLimit;
    }

    @Override
    public T execute() throws InterruptedException {
        logger.info("produces: " + howMany);
        for (int i = 0; i < howMany; i++) {
            TaskAbstractionAndStats.waitForItemToProduce();
            buffer.add(instance);
        }
        logger.info("Produced: " + howMany);
        logger.info("\t>>>: " + buffer.size());
        return instance;
    }

    @Override
    public void eventually() {
    }
}
