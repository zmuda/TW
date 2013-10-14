package pl.agh.student.mizmuda.lab1;

public class Philosopher implements Runnable {
    private int id;
    private final Semaphore left;
    private final Semaphore right;
    private final Semaphore overseer;

    Philosopher(int id, Semaphore left, Semaphore right, Semaphore overseer) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.overseer = overseer;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(id + ": now thinking");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(id + ": thinking interrupted");
            }
            overseer.P();
            left.P();

            right.P();

            System.out.println(id + ": now eating");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(id + ": sleeping interrupted");
            }
            System.out.println(id + ": eaten");
            right.V();
            left.V();
            overseer.V();
        }
    }
}

