package activeobject.activeproducersconsumers.core;


import activeobject.LongCollector;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureMethodRequest<T> extends FutureTask<T> {
    private final IMethodRequest<T> methodRequest;
    private final long nanoStamp = System.nanoTime();

    public FutureMethodRequest(final IMethodRequest<T> request, final LongCollector activeObjectExecutionTime) {
        super(new Callable<T>() {
            @Override
            public T call() throws Exception {
                long time = System.currentTimeMillis();
                T result = request.execute();
                activeObjectExecutionTime.submit(System.currentTimeMillis() - time);
                return result;
            }
        });
        this.methodRequest = request;
    }

    public long getNanoStamp() {
        return nanoStamp;
    }

    public IMethodRequest<T> getMethodRequest() {
        return methodRequest;
    }
}

