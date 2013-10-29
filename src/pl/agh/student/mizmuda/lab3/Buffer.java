package pl.agh.student.mizmuda.lab3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer implements IBuffer {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private final Queue<Integer> emptyAddresses;
    private final Queue<Integer> fullAddresses;
    private final int arraySize;
    private final Map<Integer, ElementState> states;

    public Buffer(int arraySize) {
        this.emptyAddresses = new LinkedList<Integer>();
        this.fullAddresses = new LinkedList<Integer>();
        this.states = new HashMap<Integer, ElementState>();
        this.arraySize = arraySize;
        for (int i = 0; i < arraySize; i++) {
            emptyAddresses.add(i);
            states.put(i, ElementState.EMPTY);
        }

    }

    @Override
    public int acquireEmpty() throws InterruptedException {
        lock.lock();
        while (emptyAddresses.isEmpty()) {
            notEmpty.await();
        }
        int tmp = emptyAddresses.poll();
        states.put(tmp, ElementState.BEING_FILLED);
        lock.unlock();
        return tmp;
    }

    @Override
    public void finalizeFilling(int address) {
        lock.lock();
        fullAddresses.add(address);
        states.put(address, ElementState.FULL);
        notEmpty.signal();
        lock.unlock();
    }

    @Override
    public int acquireFull() throws InterruptedException {
        lock.lock();
        while (fullAddresses.isEmpty()) {
            notFull.await();
        }
        int tmp = fullAddresses.poll();
        states.put(tmp, ElementState.BEING_EMPTIED);
        lock.unlock();
        return tmp;
    }

    @Override
    public void finalizeEmptying(int address) {
        lock.lock();
        emptyAddresses.add(address);
        states.put(address, ElementState.EMPTY);
        notFull.signal();
        lock.unlock();
    }
}
