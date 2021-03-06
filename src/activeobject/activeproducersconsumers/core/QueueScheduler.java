package activeobject.activeproducersconsumers.core;


public class QueueScheduler implements Runnable {
    private final RequestQueue queue = new RequestQueue();
    private boolean shutdown = false;

    public void queueShutdown() {
        this.shutdown = true;
    }

    public <T> void queueExecution(FutureMethodRequest<T> task) {
        queue.add(task);
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                FutureMethodRequest request = queue.peekNextRequest();
                if (request != null && request.getMethodRequest().guard()) {
                    queue.pollNextRequestWithWait();
                    request.run();
                } else {
                    request = queue.peekNextComplementary();
                    if (request != null && request.getMethodRequest().guard()) {
                        queue.pollNextComplementary();
                        request.run();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

    }
}
