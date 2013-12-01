package pl.agh.student.mizmuda.activeproducersconsumers.core;


import java.util.LinkedList;
import java.util.Queue;

public class QueueScheduler {
    private final Queue<FutureMethodRequest> queue = new LinkedList<FutureMethodRequest>();
    private FutureMethodRequest privileged;

    public <T> void queueExecution(FutureMethodRequest<T> task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    public void run() {
        while (true) {
            while (privileged == null) {
                synchronized (queue) {
                    privileged = queue.poll();
                }
            }
            if (privileged.getMethodRequest().guard()) {
                privileged.run();
                privileged = null;
            } else {
                synchronized (queue) {
                    for (FutureMethodRequest future : queue) {
                        if (future != privileged
                                && !future.getMethodRequest().getClass().equals(privileged.getMethodRequest().getClass())
                                && future.getMethodRequest().guard()) {
                            future.run();
                            queue.remove(future);
                            break;
                        }
                    }
                    try {
                        queue.wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();   //todo
                    }
                }
            }
        }
    }
}
