package activeobject.activeproducersconsumers.core;

public interface IMethodRequest<T> {

    public boolean guard();

    public T execute();

    public void eventually();
}
