package pl.agh.student.mizmuda.lab1;

public class K implements Runnable {

    private final BinarySemaphore fullB2;
    private final Semaphore emptyPlacesInB2;
    private final int bufferSize;

    public K(BinarySemaphore fullB2, Semaphore emptyPlacesInB2, int bufferSize) {
        this.fullB2 = fullB2;
        this.emptyPlacesInB2 = emptyPlacesInB2;
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        fullB2.P();
        //konsumuj
        for (int i = 0; i < bufferSize / 2; i++) {
            emptyPlacesInB2.V();
        }
    }
}
