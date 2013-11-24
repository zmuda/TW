package pl.agh.student.mizmuda.activeobject;

import java.util.Collection;

public interface IBuffer {
    Collection<Integer> acquireElements(int num) throws InterruptedException;

    Collection<Integer> acquireSpace(int num) throws InterruptedException;

    void releaseElements(Collection<Integer> positions);

    void releaseSpace(Collection<Integer> positions);

    int getMaxSize();
}
