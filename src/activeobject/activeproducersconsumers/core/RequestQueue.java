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
        } else {
            throw new IllegalArgumentException();
        }
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
            if (consumption != null) {
                classOfDesignated = consumption.getMethodRequest().getClass();
            }
            return consumption;
        } else {
            classOfDesignated = production.getMethodRequest().getClass();
            return production;
        }
    }

    public synchronized FutureMethodRequest pollNextRequest() {
        FutureMethodRequest consumption = consumptions.peek();
        FutureMethodRequest production = productions.peek();
        if (production == null || (consumption != null && consumption.getNanoStamp() < production.getNanoStamp())) {
            if (consumption != null) {
                classOfDesignated = consumption.getMethodRequest().getClass();
            }
            return consumptions.poll();
        } else {
            classOfDesignated = production.getMethodRequest().getClass();
            return productions.poll();
        }
    }

    public synchronized FutureMethodRequest pollNextComplementary() {
        if (classOfDesignated != null && classOfDesignated.equals(ConsumeRequest.class)) {
            return pollNextProduction();
        } else {
            return pollNextConsumption();
        }
    }

    public synchronized FutureMethodRequest peekNextComplementary() {
        if (classOfDesignated != null && classOfDesignated.equals(ConsumeRequest.class)) {
            return peekNextProduction();
        } else {
            return peekNextConsumption();
        }
    }
}
