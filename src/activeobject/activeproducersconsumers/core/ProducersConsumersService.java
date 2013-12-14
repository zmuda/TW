package activeobject.activeproducersconsumers.core;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Future;

public class ProducersConsumersService<T> extends Service {

    private final Queue<T> buffer = new LinkedList<T>();
    private final Random random;
    private final T instance;
    private final int bufferLimit;

    public ProducersConsumersService(int bufferLimit, T exampleInstance) {
        super(new QueueScheduler());
        this.random = new Random(System.currentTimeMillis());
        this.instance = exampleInstance;
        this.bufferLimit = bufferLimit;
    }

    public Future<T> consume() {
        IMethodRequest<T> request = new ConsumeRequest<T>(buffer);
        FutureMethodRequest<T> methodRequest = new FutureMethodRequest<T>(request);
        scheduler.queueExecution(methodRequest);
        return methodRequest;
    }

    public Future<T> produce() {
        IMethodRequest<T> request = new ProduceRequest<T>(buffer, instance, bufferLimit);
        FutureMethodRequest<T> methodRequest = new FutureMethodRequest<T>(request);
        scheduler.queueExecution(methodRequest);
        return methodRequest;
    }

}
