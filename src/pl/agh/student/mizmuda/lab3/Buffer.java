package pl.agh.student.mizmuda.lab3;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer implements IBuffer {
    private Logger logger = Logger.getLogger("lab3");
    private final ReentrantLock lockEmptyQueue = new ReentrantLock();
    private final ReentrantLock lockFullQueue = new ReentrantLock();
    private final ReentrantLock lockStatesArray = new ReentrantLock();
    private final Condition elementsAvailable = lockFullQueue.newCondition();
    private final Condition spaceAvailable = lockEmptyQueue.newCondition();
    private final Queue<Integer> emptyAddresses;
    private final Queue<Integer> fullAddresses;
    private final int arraySize;
    private ArrayList<ElementState> states;

    private void setState(int address, ElementState state) {
        lockStatesArray.lock();
        try {
            states.set(address, state);
        } finally {
            lockStatesArray.unlock();
        }
    }

    private ElementState getState(int address) {
        ElementState state;
        lockStatesArray.lock();
        try {
            state = states.get(address);
        } finally {
            lockStatesArray.unlock();
        }
        return state;
    }

    private void addNextState(ElementState state) {
        lockStatesArray.lock();
        try {
            states.add(state);
        } finally {
            lockStatesArray.unlock();
        }
    }

    public Buffer(int arraySize) {
        this.emptyAddresses = new LinkedList<Integer>();
        this.fullAddresses = new LinkedList<Integer>();
        this.states = new ArrayList<ElementState>(arraySize);
        this.arraySize = arraySize;
        for (int i = 0; i < this.arraySize; i++) {
            emptyAddresses.add(i);
            addNextState(ElementState.EMPTY);
        }
        BasicConfigurator.configure();
    }

    @Override
    public int acquireEmpty() throws InterruptedException {
        lockEmptyQueue.lock();
        int tmp;
        try {
            while (emptyAddresses.isEmpty()) {
                spaceAvailable.await();
            }
            tmp = emptyAddresses.poll();
            states.set(tmp, ElementState.BEING_FILLED);
        } finally {
            lockEmptyQueue.unlock();
        }
        logger.info(getStateString());
        return tmp;
    }

    @Override
    public void finalizeFilling(int address) {
        lockFullQueue.lock();
        try {
            if (getState(address) != ElementState.BEING_FILLED) {
                throw new IllegalStateException("acquire - finalize contract broken");
            }
            fullAddresses.add(address);
            setState(address, ElementState.FULL);
            elementsAvailable.signal();
            logger.info(getStateString());
        } finally {
            lockFullQueue.unlock();
        }
    }

    @Override
    public int acquireFull() throws InterruptedException {
        lockFullQueue.lock();
        int tmp;
        try {
            while (fullAddresses.isEmpty()) {
                elementsAvailable.await();
            }
            tmp = fullAddresses.poll();
            setState(tmp, ElementState.BEING_EMPTIED);
        } finally {
            lockFullQueue.unlock();
        }
        logger.info(getStateString());
        return tmp;
    }

    @Override
    public void finalizeEmptying(int address) {
        lockEmptyQueue.lock();
        try {
            if (getState(address) != ElementState.BEING_EMPTIED) {
                throw new IllegalStateException("acquire - finalize contract broken");
            }
            emptyAddresses.add(address);
            setState(address, ElementState.EMPTY);
            spaceAvailable.signal();
            logger.info(getStateString());
        } finally {
            lockEmptyQueue.unlock();
        }
    }

    private String getStateString() {
        StringBuffer res = new StringBuffer(this.toString());
        res.append(" - occupation - ");
        lockStatesArray.lock();
        try {
            for (ElementState e : states) {
                res.append(e.getRepresentation());
            }
        } finally {
            lockStatesArray.unlock();
        }
        return res.toString();
    }
}
