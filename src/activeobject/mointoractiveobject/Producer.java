package activeobject.mointoractiveobject;


import activeobject.TaskAbstractionAndStats;

public class Producer implements Runnable {
    private final IBuffer buffer;


    public Producer(IBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            while (i < TaskAbstractionAndStats.probeSize && !Thread.currentThread().isInterrupted()) {
                int address = buffer.acquireEmpty();
                TaskAbstractionAndStats.waitForItemToProduce();
                buffer.finalizeFilling(address);
                i++;
                TaskAbstractionAndStats.waitForSideTaskToComplete();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
