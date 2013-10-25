package pl.agh.student.mizmuda.lab2.asynchronous.v2;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer<T> {
    private final Random random = new Random(System.currentTimeMillis());
    private final int limit;
    private Logger logger;
    ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
    private ArrayList<T> data;
    private BufferState[] dataStates;
    private Queue<Integer> indicesOfProduced;
    private ConcurrentMap<Runnable, Condition> runnableConditions = new ConcurrentHashMap<Runnable, Condition>();
    private ConcurrentMap<Integer, Condition> indicesConditions = new ConcurrentHashMap<Integer, Condition>();


    public Buffer(int limit, String loggerRef) {
        this.limit = limit;
        logger = Logger.getLogger(loggerRef);
        data = new ArrayList<T>(limit);
        dataStates = new BufferState[limit];
        indicesOfProduced = new LinkedList<Integer>();
        for (int i = 0; i < limit; i++) {
            dataStates[i] = BufferState.EMPTY;
            data.add(null);
        }
    }

    public void pushElement(T element) {
        lock.lock();
        Integer index = getIndexForAdd();
        lock.unlock();
        add(index, element);
        finalizeAdd(index);
    }

    public T poolElement(Runnable requester) {
        if (!runnableConditions.containsKey(requester)) {
            runnableConditions.put(requester, lock.newCondition());
        }
        lock.lock();
        Integer index = getIndexForGet(runnableConditions.get(requester));
        lock.unlock();
        T ret = get(index);
        finalizeGet(index);
        return ret;
    }

    private T get(int index) {
        try {
            Thread.sleep(random.nextInt(100) + 1);
        } catch (InterruptedException e) {
        }
        return data.get(index);
    }

    private void add(int index, T element) {
        try {
            Thread.sleep(random.nextInt(100) + 1);
        } catch (InterruptedException e) {
        }
        data.set(index, element);
    }

    private Integer fetchIndexOfEmpty() {
        for (int i = 0; i < limit; i++) {
            if (dataStates[i] == BufferState.EMPTY) {
                dataStates[i] = BufferState.RESERVED_ADDITION;
                indicesOfProduced.add(i);
                notEmpty.signal();
                return i;
            }
        }
        return null;
    }

    private Integer fetchIndexOfFull() {
        Integer address = indicesOfProduced.poll();
        return address;
    }

    private Integer getIndexForAdd() {
        Integer address = fetchIndexOfEmpty();
        while (address == null) {
            try {
                notFull.await();
            } catch (InterruptedException e) {
            } finally {
                address = fetchIndexOfEmpty();
            }
        }
        return address;
    }

    private void finalizeAdd(int index) {
        lock.lock();
        if (dataStates[index] != BufferState.RESERVED_ADDITION) {
            throw new IllegalAccessError("Add contract violated");
        }
        dataStates[index] = BufferState.OCCUPIED;
        logger.info("add finished:\t\t" + getString());
        if (indicesConditions.containsKey(index)) {
            indicesConditions.get(index).signal();
        }
        lock.unlock();
    }

    private Integer getIndexForGet(Condition requesterCondition) {
        Integer address = fetchIndexOfFull();
        while (address == null) {
            try {
                notEmpty.await();
            } catch (InterruptedException e) {
            } finally {
                address = fetchIndexOfFull();
            }
        }
        logger.info("\tassigned for get " + getString());
        while (dataStates[address] != BufferState.OCCUPIED) {
            indicesConditions.put(address, requesterCondition);
            try {
                requesterCondition.await();
            } catch (InterruptedException e) {
            }
        }
        dataStates[address] = BufferState.RESERVED_DELETION;
        if (indicesConditions.containsKey(address)) {
            indicesConditions.remove(address);
        }
        logger.info("\treturned for get " + getString());
        return address;
    }

    private void finalizeGet(int address) {
        lock.lock();
        if (dataStates[address] != BufferState.RESERVED_DELETION) {
            throw new IllegalAccessError("Get contract violated");
        }
        dataStates[address] = BufferState.EMPTY;
        logger.info("get finished:\t\t" + getString());
        notFull.signal();
        lock.unlock();
    }

    public String getString() {
        StringBuffer res = new StringBuffer("[");
        for (BufferState state : dataStates) {
            res.append(state.getRepresentation());
        }
        res.append(("]"));
        return res.toString();
    }

    private enum BufferState {
        OCCUPIED("■"),
        EMPTY("□"),
        RESERVED_ADDITION("◊"),
        RESERVED_DELETION("♦");
        private String representation;

        BufferState(String s) {
            representation = s;
        }

        public String getRepresentation() {
            return representation;
        }
    }
}
