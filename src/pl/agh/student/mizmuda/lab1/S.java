package pl.agh.student.mizmuda.lab1;

public class S implements Runnable{

    private final BinarySemaphore fullB2;
    private final Semaphore emptyPlacesInB2;
    private final Semaphore availablePairsInB1;
    private final Semaphore emptyPairsInB1;
    private final int bufferSize;

    public S(BinarySemaphore fullB2, Semaphore emptyPlacesInB2, Semaphore availablePairsInB1, Semaphore emptyPairsInB1, int bufferSize) {
        this.fullB2 = fullB2;
        this.emptyPlacesInB2 = emptyPlacesInB2;
        this.availablePairsInB1 = availablePairsInB1;
        this.emptyPairsInB1 = emptyPairsInB1;
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
        emptyPlacesInB2.P();
        availablePairsInB1.P();
        //rob swoje
        if(/*pelne*/){
            fullB2.V();
        }
    }
}
