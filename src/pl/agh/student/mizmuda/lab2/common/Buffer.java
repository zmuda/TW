package pl.agh.student.mizmuda.lab2.common;

public interface Buffer {
    public void pushElement(Integer element) throws InterruptedException;

    public Integer poolElement() throws InterruptedException;
}
