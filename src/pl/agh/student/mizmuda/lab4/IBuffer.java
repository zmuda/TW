package pl.agh.student.mizmuda.lab4;

import java.util.Collection;

public interface IBuffer<T> {
    Collection<T> pool(int num) throws InterruptedException;

    void push(Collection<T> elements) throws InterruptedException;

    int getMaxSize();
}
