package activeobject.activeproducersconsumers.core;

import java.util.LinkedList;
import java.util.Queue;

public class RequestQueue {
    private final Queue<FutureMethodRequest> consumptions = new LinkedList<FutureMethodRequest>();
    private final Queue<FutureMethodRequest> productions = new LinkedList<FutureMethodRequest>();
    private boolean consumerIsFirst = false;

    public synchronized void add(FutureMethodRequest req) {
        if (req.getMethodRequest() instanceof ConsumeRequest) {
            consumptions.add(req);
        } else if (req.getMethodRequest() instanceof ProduceRequest) {
            productions.add(req);
        } else {
            throw new IllegalArgumentException();
        }
        this.notify();
    }

    public synchronized FutureMethodRequest peekNextConsumption() {
        return consumptions.peek();
    }

    public synchronized FutureMethodRequest peekNextProduction() {
        return productions.peek();
    }

    public synchronized FutureMethodRequest pollNextConsumption() {
        return consumptions.poll();
    }

    public synchronized FutureMethodRequest pollNextProduction() {
        return productions.poll();
    }

    public synchronized FutureMethodRequest peekNextRequest() {
        FutureMethodRequest consumption = consumptions.peek();
        FutureMethodRequest production = productions.peek();
        if (production == null || (consumption != null && consumption.getNanoStamp() < production.getNanoStamp())) {
            consumerIsFirst = true;
            return consumption;
        } else {
            consumerIsFirst = false;
            return production;
        }
    }

    public synchronized FutureMethodRequest pollNextRequestWithWait() throws InterruptedException {
        while (consumptions.isEmpty() && productions.isEmpty()) {
            this.wait();
        }
        FutureMethodRequest consumption = consumptions.peek();
        FutureMethodRequest production = productions.peek();
        if (production == null || (consumption != null && consumption.getNanoStamp() < production.getNanoStamp())) {
            consumerIsFirst = true;
            return consumptions.poll();
        } else {
            consumerIsFirst = false;
            return productions.poll();
        }
    }

    public synchronized FutureMethodRequest pollNextComplementary() {
        if (consumerIsFirst) {
            return pollNextProduction();
        } else {
            return pollNextConsumption();
        }
    }

    public synchronized FutureMethodRequest peekNextComplementary() {
        if (consumerIsFirst) {
            return peekNextProduction();
        } else {
            return peekNextConsumption();
        }
    }
}
