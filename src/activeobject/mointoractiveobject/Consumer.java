package activeobject.mointoractiveobject;


import activeobject.TaskDurations;

public class Consumer implements Runnable {
    private final IBuffer buffer;

    public Consumer(IBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            while (i < TaskDurations.probeSize && !Thread.currentThread().isInterrupted()) {
                int address = buffer.acquireFull();
                TaskDurations.waitForItemToConsume();
                buffer.finalizeEmptying(address);
                i++;
                TaskDurations.waitForSideTaskToComplete();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
