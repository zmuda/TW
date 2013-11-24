package pl.agh.student.mizmuda.classics.monitor.zad_4_3_1;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ResourcePool {
    private final Queue<Integer> queueA;
    private final Queue<Integer> queueB;
    private ReentrantLock lock = new ReentrantLock();
    private Condition A = lock.newCondition();
    private Condition B = lock.newCondition();
    private Condition AB = lock.newCondition();
    private Condition eligibleForAB = lock.newCondition();

    public ResourcePool(int quantityA, int quantityB) {
        queueA = new LinkedList<Integer>();
        queueB = new LinkedList<Integer>();
        for (int i = 0; i < quantityA; i++) {
            queueA.add(i);
        }
        for (int i = 0; i < quantityB; i++) {
            queueB.add(i);
        }
    }

    public int getA() throws InterruptedException {
        lock.lock();
        int ret = -1;
        try {
            while (queueA.size() == 0) {
                A.await();
            }
            ret = queueA.remove();
        } finally {
            lock.unlock();
        }
        return ret;
    }

    public void returnA(int address) {
        lock.lock();
        queueA.add(address);
        if (lock.hasWaiters(eligibleForAB)) {
            eligibleForAB.signal();
        } else {
            AB.signal();
        }
    }

    public int getB() throws InterruptedException {
        lock.lock();
        int ret = -1;
        try {
            while (queueB.size() == 0) {
                B.await();
            }
            ret = queueB.remove();
        } finally {
            lock.unlock();
        }
        return ret;
    }

    public void returnB(int address) {
        lock.lock();
        queueB.add(address);
        if (lock.hasWaiters(eligibleForAB)) {
            eligibleForAB.signal();
        } else if (lock.hasWaiters(AB)) {
            AB.signal();
        } else {
            A.signal();
            B.signal();
        }
        lock.unlock();
    }

    public int[] getAB() {
        lock.lock();
        int[] ret = {-1, -1};
        try {
            while (lock.hasWaiters(eligibleForAB)) {
                AB.await();
            }
            while ((ret[0] == -1 && queueA.size() == 0) || (ret[1] == -1 && queueB.size() == 0)) {
                eligibleForAB.await();
                if (queueA.size() > 0 && ret[0] == -1) {
                    ret[0] = queueA.remove();
                    A.signal();//other processes might want to use it
                }
                if (queueB.size() > 0 && ret[1] == -1) {
                    ret[1] = queueB.remove();
                    B.signal();//other processes might want to use it as well
                }
            }
        } catch (InterruptedException caught) {
            if (ret[0] != -1) {
                queueA.add(ret[0]);
            }
            if (ret[1] != -1) {
                queueB.add(ret[1]);
            }
        } finally {
            lock.unlock();
        }
        return ret;
    }

    public void returnAB(int[] addresses) {
        lock.lock();
        queueA.add(addresses[0]);
        queueB.add(addresses[1]);
        if (lock.hasWaiters(A) || lock.hasWaiters(B)) {
            A.signal();
            B.signal();
        } else if (lock.hasWaiters(eligibleForAB)) {
            eligibleForAB.signal();
        } else {
            AB.signal();
        }
        lock.unlock();
    }
}
