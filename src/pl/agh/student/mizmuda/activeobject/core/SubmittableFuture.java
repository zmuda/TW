package pl.agh.student.mizmuda.activeobject.core;


import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SubmittableFuture<T> implements ISubmittable<T>, Future<T> {
    private final ISubmittable<T> task;
    private T result = null;

    public SubmittableFuture(ISubmittable<T> task) {
        this.task = task;
    }

    @Override
    public Collection<Object> resourcesProduced() {
        return task.resourcesProduced();
    }

    @Override
    public Collection<Object> resourcesConsumed() {
        return task.resourcesConsumed();
    }

    @Override
    public boolean test() {
        return task.test();
    }

    @Override
    //@Deprecated
    public T execute() {
        //throw new IllegalAccessError("pl.agh.student.mizmuda.activeobject.core.SubmittableFuture supports evaluation like FutureTask only");
        T value = task.execute();
        set(value);
        return value;
    }

    @Override
    public void eventually() {
        task.eventually();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return (result != null);
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        synchronized (task) {
            while (!isDone()) {
                task.wait();
            }
        }
        return result;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (unit != TimeUnit.MILLISECONDS) {
            throw new UnsupportedOperationException();
        }
        synchronized (task) {
            if (!isDone()) {
                task.wait(timeout);
            }
        }
        return result;
    }

    private void set(T value) {
        synchronized (task) {
            result = value;
            task.notifyAll();
        }
    }
}
