package pl.agh.student.mizmuda.activeproducersconsumers.core;

public interface IMethodRequest<T> {

    public boolean guard();

    public T execute();

    public void eventually();
}
