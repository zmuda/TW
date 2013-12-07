package activeobject.mointoractiveobject;


import activeobject.LongCollecter;
import activeobject.TaskDuration;

public class Consumer implements Runnable {
    private final IBuffer buffer;
    private LongCollecter executionTimes;
    private long totalSpent;

    public Consumer(IBuffer buffer, LongCollecter executionTimes) {
        this.buffer = buffer;
        this.executionTimes = executionTimes;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            totalSpent = -System.currentTimeMillis();
            while (i < TaskDuration.probeSize && !Thread.currentThread().isInterrupted()) {
                int address = buffer.acquireFull();
                TaskDuration.waitForItemToConsume();
                buffer.finalizeEmptying(address);
                i++;
            }
            totalSpent += System.currentTimeMillis();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executionTimes.submit(totalSpent);
        }
    }
}
