package pl.agh.student.mizmuda.lab4;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer<T> implements IBuffer<T> {
    private Logger logger = Logger.getLogger("lab4");
    private final Queue<T> data;
    private int size;
    private final int maxSize;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition firstProducer = lock.newCondition();
    private final Condition firstConsumer = lock.newCondition();
    private final Condition producers = lock.newCondition();
    private final Condition consumers = lock.newCondition();
    private boolean firstProducerWaits = false;
    private boolean firstConsumerWaits = false;

    public Buffer(int maxSize) {
        assert maxSize % 2 == 0;
        this.data = new LinkedList<T>();
        this.maxSize = maxSize;
        this.size = 0;
        BasicConfigurator.configure();
    }

    @Override
    public Collection<T> pool(int num) throws InterruptedException {
        lock.lock();
        Collection<T> res = new ArrayList<T>();
        try {
            while (firstConsumerWaits) {
                consumers.await();
            }
            while (size < num) {
                firstConsumerWaits = true;
                firstConsumer.await();
            }
            size -= num;
            for (int i = 0; i < num; i++) {
                res.add(data.poll());
            }
            firstConsumerWaits = false;
            logger.info("Returned " + num + " elements - now contains " + size + " out of " + maxSize);
            consumers.signal();
            firstProducer.signal();
        } finally {
            lock.unlock();
        }
        return res;
    }

    @Override
    public void push(Collection<T> elements) throws InterruptedException {
        int elementsSize = elements.size();
        lock.lock();
        try {
            while (firstProducerWaits) {
                producers.await();
            }
            while (maxSize - size < elementsSize) {
                firstProducerWaits = true;
                firstProducer.await();
            }
            size += elementsSize;       //this is
            data.addAll(elements);      //on
            firstProducerWaits = false; //purpose
            logger.info("Added " + elementsSize + " elements - now contains " + size + " out of " + maxSize);
            producers.signal();
            firstConsumer.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }
}
