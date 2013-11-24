package pl.agh.student.mizmuda.classics.semaphore.philosophers;


import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class TableWithWaiter {
    private final Semaphore waiter = new Semaphore(4);
    private final ArrayList<Semaphore> forks;

    public TableWithWaiter() {
        forks = new ArrayList<Semaphore>();
        for (int i = 0; i < 5; i++) {
            forks.add(new Semaphore(1));
        }
    }

    public void startEating(int self) throws InterruptedException {
        waiter.acquire();
        try {
            forks.get(self).acquire();
        } catch (InterruptedException caught) {
            waiter.release();
            throw caught;
        }
        try {
            forks.get((self + 1) % 5).acquire();
        } catch (InterruptedException caught) {
            waiter.release();
            forks.get(self).release();
            throw caught;
        }

    }

    public void finishEating(int self) {
        waiter.release();
        forks.get(self).release();
        forks.get((self + 1) % 5).release();
    }
}
