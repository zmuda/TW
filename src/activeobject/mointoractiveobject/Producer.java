package activeobject.mointoractiveobject;


import activeobject.TaskDuration;

public class Producer implements Runnable {
    private final IBuffer buffer;


    public Producer(IBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                int address = buffer.acquireEmpty();
                TaskDuration.waitForItemToProduce();
                buffer.finalizeFilling(address);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
