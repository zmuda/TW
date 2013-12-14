package activeobject.mointoractiveobject;


import activeobject.TaskDurations;

public class Producer implements Runnable {
    private final IBuffer buffer;


    public Producer(IBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            while (i < TaskDurations.probeSize && !Thread.currentThread().isInterrupted()) {
                int address = buffer.acquireEmpty();
                TaskDurations.waitForItemToProduce();
                buffer.finalizeFilling(address);
                i++;
                TaskDurations.waitForSideTaskToComplete();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
