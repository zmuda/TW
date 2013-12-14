package activeobject.activeproducersconsumers.core;


import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureMethodRequest<T> extends FutureTask<T> {
    private final IMethodRequest<T> methodRequest;
    private final long nanoStamp = System.nanoTime();

    public FutureMethodRequest(final IMethodRequest<T> request) {
        super(new Callable<T>() {
            @Override
            public T call() throws Exception {
                T result = request.execute();
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

