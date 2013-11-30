package pl.agh.student.mizmuda.activeobject.core;


import java.util.concurrent.Future;

public class SubmittableExecutor implements ISubmittableExecutor {
    public Runnable getWorker() {
        return worker;
    }

    private final ISubmittableWorker worker = new SubmittableWorker();

    @Override
    public <T> Future<T> submit(ISubmittable<T> task) {
        SubmittableFuture<T> result = new SubmittableFuture<T>(task);
        worker.queueTask(result);
        return result;
    }
}
