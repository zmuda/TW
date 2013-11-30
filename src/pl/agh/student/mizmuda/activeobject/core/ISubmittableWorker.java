package pl.agh.student.mizmuda.activeobject.core;


public interface ISubmittableWorker extends Runnable {
    public <T> void queueTask(SubmittableFuture<T> task);
}
