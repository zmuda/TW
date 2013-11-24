package pl.agh.student.mizmuda.classics.monitor.zad_4_3_2;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProductionLine {
    private ReentrantLock lock = new ReentrantLock();
    private final Condition[] conditions;
    private final Queue[] queues;
    private final int nrOfPhases;

    public ProductionLine(int lineSlots, int nrOfPhases) {
        this.nrOfPhases = nrOfPhases;
        conditions = new Condition[lineSlots];
        for (int i = 0; i < lineSlots; i++) {
            conditions[i] = lock.newCondition();
        }
        queues = new Queue[nrOfPhases];
        for (int i = 0; i < lineSlots; i++) {
            queues[i] = new LinkedList();
        }
    }

    public int startPhase(int nr) throws InterruptedException {
        lock.lock();
        int ret = -1;
        try {
            while (queues[nr].size() == 0) {
                conditions[nr].await();
            }
            ret = (Integer) queues[nr].remove();
        } finally {
            lock.unlock();
        }
        return ret;
    }

    public void finishPhase(int nr, int address) {
        lock.lock();
        int next = (nr + 1) % nrOfPhases;
        queues[next].add(address);
        conditions[next].signal();
        lock.unlock();
    }
}
