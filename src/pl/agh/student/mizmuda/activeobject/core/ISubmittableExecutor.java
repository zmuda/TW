package pl.agh.student.mizmuda.activeobject.core;


import java.util.concurrent.Future;

public interface ISubmittableExecutor {
    public <T> Future<T> submit(ISubmittable<T> task);

    public Runnable getWorker();
}
