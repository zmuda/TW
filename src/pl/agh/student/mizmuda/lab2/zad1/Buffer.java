package pl.agh.student.mizmuda.lab2.zad1;

public class Buffer {
    private Integer data = null;

    public synchronized void pushElement(Integer element) {
        while (data != null) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        data = element;
        notify();
    }

    public synchronized Integer popElement() {
        while (data == null) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        notify();
        Integer tmp = data;
        data = null;
        return tmp;
    }

}
