package activeobject.activeproducersconsumers.core;


import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureMethodRequest<T> extends FutureTask<T> {

    private final IMethodRequest<T> methodRequest;

    public FutureMethodRequest(final IMethodRequest<T> request) {
        super(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return request.execute();
            }
        });
        this.methodRequest = request;
    }

    public IMethodRequest<T> getMethodRequest() {
        return methodRequest;
    }
}

