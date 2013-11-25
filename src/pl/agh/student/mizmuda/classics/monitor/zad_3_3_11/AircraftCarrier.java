package pl.agh.student.mizmuda.classics.monitor.zad_3_3_11;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AircraftCarrier {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition takeOff = lock.newCondition();
    private final Condition land = lock.newCondition();
    private final int capacity;
    private final int priorityBorder;
    private int aboard = 0;
    private boolean runwayAvailable = true;

    public AircraftCarrier(int capacity, int priorityBorder) {
        this.capacity = capacity;
        this.priorityBorder = priorityBorder;
        assert priorityBorder < capacity;
    }

    private boolean isLandingPriority() {
        return aboard < priorityBorder;
    }

    public void startTakingOff() throws InterruptedException {
        lock.lock();
        try {
            while (!runwayAvailable && (isLandingPriority() && lock.hasWaiters(land))) {
                takeOff.await();
            }
            runwayAvailable = false;
        } finally {
            lock.unlock();
        }
    }

    public void takingOffFinished() {
        lock.lock();
        aboard--;
        runwayAvailable = true;
        if (isLandingPriority()) {
            land.signal();
        } else {
            takeOff.signal();
        }
        lock.unlock();
    }

    public void startLanding() throws InterruptedException {
        lock.lock();
        try {
            while (!runwayAvailable && (!isLandingPriority() && lock.hasWaiters(takeOff))) {
                land.await();
            }
            runwayAvailable = false;
        } finally {
            lock.unlock();
        }
    }

    public void landingFinished() {
        lock.lock();
        aboard++;
        runwayAvailable = true;
        if (isLandingPriority()) {
            land.signal();
        } else {
            takeOff.signal();
        }
        lock.unlock();
    }

}
