package pl.agh.student.mizmuda.activeproducersconsumers.problemspecific;


import org.apache.log4j.Logger;
import pl.agh.student.mizmuda.activeproducersconsumers.core.IMethodRequest;

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
    public T execute() {
        logger.info("produces: " + howMany);
        for (int i = 0; i < howMany; i++) {
            /* keeping busy */
            int tmp = random.nextInt(40);
            while (tmp > 0) {
                tmp = random.nextInt(40);
            }
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
