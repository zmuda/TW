package activeobject.mointoractiveobject;


import activeobject.TaskDuration;

public class Consumer implements Runnable {
    private final IBuffer buffer;

    public Consumer(IBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            while (i < TaskDuration.probeSize && !Thread.currentThread().isInterrupted()) {
                int address = buffer.acquireFull();
                TaskDuration.waitForItemToConsume();
                buffer.finalizeEmptying(address);
                i++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
