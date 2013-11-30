package pl.agh.student.mizmuda.activeobject.core;


import java.util.LinkedList;

public class SubmittableWorker implements ISubmittableWorker {
    private final LinkedList<SubmittableFuture> queue = new LinkedList<SubmittableFuture>();
    private SubmittableFuture privileged;

    @Override
    public <T> void queueTask(SubmittableFuture<T> task) {
        synchronized (queue) {
            queue.addFirst(task);
        }
    }

    private boolean concernsCross(SubmittableFuture privileged, SubmittableFuture pending) {
        return false;
    }

    @Override
    public void run() {
        while (true) {
            while (privileged == null) {
                synchronized (queue) {
                    privileged = queue.poll();
                }
            }
            if (privileged.test()) {
                privileged.execute();
                privileged = null;
            } else {
                synchronized (queue) {
                    for (SubmittableFuture future : queue) {
                        if (future != privileged && !concernsCross(privileged, future) && future.test()) {
                            future.execute();
                            queue.remove(future);
                            break;
                        }
                    }
                }
            }
        }
    }
}
