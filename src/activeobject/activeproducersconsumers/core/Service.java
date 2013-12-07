package activeobject.activeproducersconsumers.core;

public abstract class Service implements Runnable {
    protected final QueueScheduler scheduler;

    protected Service(QueueScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void run() {
        scheduler.run();
    }

    public void shutdown() {
        scheduler.queueShutdown();
    }

}
