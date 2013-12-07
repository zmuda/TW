package activeobject.mointoractiveobject;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer implements IBuffer {
    private Logger logger = Logger.getLogger("mointoractiveobject");
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition elementsAvailable = lock.newCondition();
    private final Condition spaceAvailable = lock.newCondition();
    private final Queue<Integer> emptyAddresses;
    private final Queue<Integer> fullAddresses;
    private final int arraySize;
    private ArrayList<ElementState> states;


    public Buffer(int arraySize) {
        this.emptyAddresses = new LinkedList<Integer>();
        this.fullAddresses = new LinkedList<Integer>();
        this.states = new ArrayList<ElementState>(arraySize);
        this.arraySize = arraySize;
        for (int i = 0; i < this.arraySize; i++) {
            emptyAddresses.add(i);
            states.add(ElementState.EMPTY);
        }
        BasicConfigurator.configure();
    }

    @Override
    public int acquireEmpty() throws InterruptedException {
        lock.lock();
        int tmp;
        try {
            while (emptyAddresses.isEmpty()) {
                spaceAvailable.await();
            }
            tmp = emptyAddresses.poll();
            states.set(tmp, ElementState.BEING_FILLED);
            logger.info(getStateString());
        } finally {
            lock.unlock();
        }
        return tmp;
    }

    @Override
    public void finalizeFilling(int address) {
        lock.lock();
        try {
            if (states.get(address) != ElementState.BEING_FILLED) {
                throw new IllegalStateException("acquire - finalize contract broken");
            }
            fullAddresses.add(address);
            states.set(address, ElementState.FULL);
            elementsAvailable.signal();
            logger.info(getStateString());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int acquireFull() throws InterruptedException {
        lock.lock();
        int tmp;
        try {
            while (fullAddresses.isEmpty()) {
                elementsAvailable.await();
            }
            tmp = fullAddresses.poll();
            states.set(tmp, ElementState.BEING_EMPTIED);
            logger.info(getStateString());
        } finally {
            lock.unlock();
        }
        return tmp;
    }

    @Override
    public void finalizeEmptying(int address) {
        lock.lock();
        try {
            if (states.get(address) != ElementState.BEING_EMPTIED) {
                throw new IllegalStateException("acquire - finalize contract broken");
            }
            emptyAddresses.add(address);
            states.set(address, ElementState.EMPTY);
            spaceAvailable.signal();
            logger.info(getStateString());
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
}
