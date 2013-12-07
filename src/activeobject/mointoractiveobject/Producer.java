package activeobject.mointoractiveobject;


import activeobject.TaskDuration;

public class Producer implements Runnable {
    private final IBuffer buffer;
    private long totalSpent;


    public Producer(IBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            totalSpent = -System.currentTimeMillis();
            while (i < TaskDuration.probeSize && !Thread.currentThread().isInterrupted()) {
                int address = buffer.acquireEmpty();
                TaskDuration.waitForItemToProduce();
                buffer.finalizeFilling(address);
                i++;
            }
            totalSpent += System.currentTimeMillis();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
