package activeobject.activeproducersconsumers.core;

import java.util.LinkedList;
import java.util.Queue;

public class RequestQueue {
    private final Queue<FutureMethodRequest> consumptions = new LinkedList<FutureMethodRequest>();
    private final Queue<FutureMethodRequest> productions = new LinkedList<FutureMethodRequest>();
    private Class classOfDesignated;

    public synchronized void add(FutureMethodRequest req) {
        if (req.getMethodRequest() instanceof ConsumeRequest) {
            consumptions.add(req);
        } else if (req.getMethodRequest() instanceof ProduceRequest) {
            productions.add(req);
        }
        throw new IllegalArgumentException();
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
        if (consumption.getNanoStamp() < production.getNanoStamp()) {
            classOfDesignated = consumption.getClass();
            return consumption;
        } else {
            classOfDesignated = production.getClass();
            return production;
        }
    }

    public synchronized FutureMethodRequest pollNextRequest() {
        FutureMethodRequest consumption = consumptions.peek();
        FutureMethodRequest production = productions.peek();
        if (consumption.getNanoStamp() < production.getNanoStamp()) {
            classOfDesignated = consumption.getClass();
            return consumptions.poll();
        } else {
            classOfDesignated = production.getClass();
            return productions.poll();
        }
    }

    public synchronized FutureMethodRequest pollNextComplementary() {
        if (classOfDesignated.isInstance(ConsumeRequest.class)) {
            return pollNextProduction();
        } else {
            return pollNextConsumption();
        }
    }

    public synchronized FutureMethodRequest peekNextComplementary() {
        if (classOfDesignated.isInstance(ConsumeRequest.class)) {
            return peekNextProduction();
        } else {
            return peekNextConsumption();
        }
    }
}
