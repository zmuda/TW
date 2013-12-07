package activeobject.activeproducersconsumers.core;

public abstract class Service {
    protected final QueueScheduler scheduler;

    protected Service(QueueScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void start() {
        scheduler.run();
    }

}
