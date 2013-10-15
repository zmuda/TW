package pl.agh.student.mizmuda.lab1prep;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhilosophersMain {

    public static void main(String[] args) throws InterruptedException {
        Semaphore forks[] = {new LockSemaphore(1), new LockSemaphore(1), new LockSemaphore(1), new LockSemaphore(1), new LockSemaphore(1)};
        Semaphore overseer = new LockSemaphore(4);
        ExecutorService service = Executors.newFixedThreadPool(5);
        service.submit(new Philosopher(1, forks[0], forks[1], overseer));
        service.submit(new Philosopher(2, forks[1], forks[2], overseer));
        service.submit(new Philosopher(3, forks[2], forks[3], overseer));
        service.submit(new Philosopher(4, forks[3], forks[4], overseer));
        service.submit(new Philosopher(5, forks[4], forks[0], overseer));

        Thread.sleep(10000);
        service.shutdownNow();
    }

}
