package activeobject.activeproducersconsumers.problemspecific;

import activeobject.LongCollecter;
import activeobject.activeproducersconsumers.core.FutureMethodRequest;
import activeobject.activeproducersconsumers.core.IMethodRequest;
import activeobject.activeproducersconsumers.core.QueueScheduler;
import activeobject.activeproducersconsumers.core.Service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Future;

public class ProducersConsumersService<T> extends Service {

    private final Queue<T> buffer = new LinkedList<T>();
    private final Random random;
    private final T instance;
    private final int bufferLimit;
    private LongCollecter activeObjectExecutionTime;

    public ProducersConsumersService(int bufferLimit, T exampleInstance, LongCollecter activeObjectExecutionTime) {
        super(new QueueScheduler());
        this.activeObjectExecutionTime = activeObjectExecutionTime;
        this.random = new Random(System.currentTimeMillis());
        this.instance = exampleInstance;
        this.bufferLimit = bufferLimit;
    }

    public Future<T> consume(int howMany) {
        IMethodRequest<T> request = new ConsumeRequest<T>(buffer, howMany, random);
        FutureMethodRequest<T> methodRequest = new FutureMethodRequest<T>(request, activeObjectExecutionTime);
        scheduler.queueExecution(methodRequest);
        return methodRequest;
    }

    public Future<T> produce(int howMany) {
        IMethodRequest<T> request = new ProduceRequest<T>(buffer, howMany, instance, bufferLimit, random);
        FutureMethodRequest<T> methodRequest = new FutureMethodRequest<T>(request, activeObjectExecutionTime);
        scheduler.queueExecution(methodRequest);
        return methodRequest;
    }

}
