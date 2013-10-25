package pl.agh.student.mizmuda.lab2.asynchronous.v1;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer<T> {
    private final Random random = new Random(System.currentTimeMillis());
    private final int limit;
    private Logger logger;
    ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private ArrayList<T> data;
    private BufferState[] dataStates;


    public Buffer(int limit, String loggerRef) {
        this.limit = limit;
        logger = Logger.getLogger(loggerRef);
        data = new ArrayList<T>(limit);
        dataStates = new BufferState[limit];
        for (int i = 0; i < limit; i++) {
            dataStates[i] = BufferState.EMPTY;
            data.add(null);
        }
    }

    public void pushElement(T element) {
        lock.lock();
        Integer index = getIndexForAdd();
        lock.unlock();
        if (index != null) {
            add(index, element);
            finalizeAdd(index);
        }
    }

    public T poolElement() {
        lock.lock();
        Integer index = getIndexForGet();
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
                dataStates[i] = BufferState.RESERVED;
                return i;
            }
        }
        return null;
    }

    private Integer fetchIndexOfFull() {
        for (int i = 0; i < limit; i++) {
            if (dataStates[i] == BufferState.OCCUPIED) {
                return i;
            }
        }
        return null;
    }

    private Integer getIndexForAdd() {
        Integer address = fetchIndexOfEmpty();
        if (address == null) {
            logger.info("skipping addition");
        } else {
            logger.info("reserved space\t\t" + getString());
        }
        return address;
    }

    private void finalizeAdd(int index) {
        lock.lock();
        if (dataStates[index] != BufferState.RESERVED) {
            throw new IllegalAccessError("Add contract violated");
        }
        dataStates[index] = BufferState.OCCUPIED;
        logger.info("add finished:\t\t" + getString());
        notEmpty.signal();
        lock.unlock();
    }

    private Integer getIndexForGet() {
        Integer address = fetchIndexOfFull();
        while (address == null) {
            try {
                notEmpty.await();
            } catch (InterruptedException e) {
            } finally {
                address = fetchIndexOfFull();
            }
        }
        logger.info("\treturned for get: " + address);
        return address;
    }

    private void finalizeGet(int address) {
        lock.lock();
        if (dataStates[address] != BufferState.OCCUPIED) {
            throw new IllegalAccessError("Get contract violated");
        }
        dataStates[address] = BufferState.EMPTY;
        logger.info("get finished:\t\t" + getString());
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
        RESERVED("♦");
        private String representation;

        BufferState(String s) {
            representation = s;
        }

        public String getRepresentation() {
            return representation;
        }
    }
}
