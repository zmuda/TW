package activeobject.mointoractiveobject;


import activeobject.TaskAbstractionAndStats;

public class Consumer implements Runnable {
    private final IBuffer buffer;

    public Consumer(IBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            while (i < TaskAbstractionAndStats.probeSize && !Thread.currentThread().isInterrupted()) {
                int address = buffer.acquireFull();
                TaskAbstractionAndStats.waitForItemToConsume();
                buffer.finalizeEmptying(address);
                i++;
                TaskAbstractionAndStats.waitForSideTaskToComplete();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
