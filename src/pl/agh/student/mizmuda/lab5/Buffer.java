package pl.agh.student.mizmuda.lab5;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer implements IBuffer {
    private Logger logger = Logger.getLogger("lab5");

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition restForElements = lock.newCondition();
    private final Condition restForSpace = lock.newCondition();
    private final Condition firstForElements = lock.newCondition();
    private final Condition firstForSpace = lock.newCondition();

    private final Queue<Integer> emptyAddresses;
    private final Queue<Integer> fullAddresses;
    private final int arraySize;
    private ArrayList<ElementState> states;


    public Buffer(int arraySize) {
        this.emptyAddresses = new LinkedList<Integer>();
        this.fullAddresses = new LinkedList<Integer>();
        this.states = new ArrayList<ElementState>(arraySize);
        this.arraySize = arraySize;
        assert arraySize % 2 == 0;
        for (int i = 0; i < this.arraySize; i++) {
            emptyAddresses.add(i);
            states.add(ElementState.EMPTY);
        }
        BasicConfigurator.configure();
    }

    @Override
    public Collection<Integer> acquireSpace(int num) throws InterruptedException {
        lock.lock();
        Collection<Integer> addresses = new LinkedList<Integer>();
        try {
            while (lock.hasWaiters(firstForSpace)) {
                restForSpace.await();
            }
            while (emptyAddresses.size() < num) {
                firstForSpace.await();
            }
            while (addresses.size() < num) {
                Integer tmp = emptyAddresses.poll();
                addresses.add(tmp);
                states.set(tmp, ElementState.BEING_FILLED);
            }
            logger.info(getStateString());
            restForSpace.signal();
        } finally {
            lock.unlock();
        }
        return addresses;
    }

    @Override
    public Collection<Integer> acquireElements(int num) throws InterruptedException {
        lock.lock();
        Collection<Integer> addresses = new LinkedList<Integer>();
        try {
            while (lock.hasWaiters(firstForElements)) {
                restForElements.await();
            }
            while (fullAddresses.size() < num) {
                firstForElements.await();
            }
            while (addresses.size() < num) {
                Integer tmp = fullAddresses.poll();
                addresses.add(tmp);
                states.set(tmp, ElementState.BEING_EMPTIED);
            }
            logger.info(getStateString());
            restForElements.signal();
        } finally {
            lock.unlock();
        }
        return addresses;
    }

    @Override
    public void releaseElements(Collection<Integer> positions) {
        lock.lock();
        try {
            for (Integer address : positions) {
                if (states.get(address) != ElementState.BEING_EMPTIED) {
                    throw new IllegalStateException("acquire - release contract broken");
                }
                emptyAddresses.add(address);
                states.set(address, ElementState.EMPTY);
            }
            logger.info(getStateString());
            firstForSpace.signal();
            //restForSpace.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void releaseSpace(Collection<Integer> positions) {
        lock.lock();
        try {
            for (Integer address : positions) {
                if (states.get(address) != ElementState.BEING_FILLED) {
                    throw new IllegalStateException("acquire - release contract broken");
                }
                fullAddresses.add(address);
                states.set(address, ElementState.FULL);
            }
            logger.info(getStateString());
            firstForElements.signal();
            //restForElements.signal();
        } finally {
            lock.unlock();
        }
    }

    private String getStateString() {
        StringBuffer res = new StringBuffer(this.toString());
        res.append(" - occupation - ");
        for (ElementState e : states) {
            res.append(e.getRepresentation());
        }
        return res.toString();
    }

    @Override
    public int getMaxSize() {
        return arraySize;
    }
}
