package activeobject.mointoractiveobject;

public interface IBuffer {
    int acquireEmpty() throws InterruptedException;

    void finalizeFilling(int address);

    int acquireFull() throws InterruptedException;

    void finalizeEmptying(int address);
}
