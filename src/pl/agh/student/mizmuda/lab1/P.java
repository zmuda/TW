package pl.agh.student.mizmuda.lab1;

public class P implements Runnable{

    private final BinarySemaphore accessInsertingB1;
    private final Semaphore availablePairsInB1;
    private final Semaphore emptyPairsInB1;
    private final int bufferSize;
    private int positionInB1;

    public P(BinarySemaphore accessInsertingB1, Semaphore availablePairsInB1, Semaphore emptyPairsInB1, int bufferSize, int positionInB1) {
        this.accessInsertingB1 = accessInsertingB1;
        this.availablePairsInB1 = availablePairsInB1;
        this.emptyPairsInB1 = emptyPairsInB1;
        this.bufferSize = bufferSize;
        this.positionInB1 = positionInB1;
    }

    @Override
    public void run() {
        //produkuj
        accessInsertingB1.P();
        //dodaj
        if(/*parzyscie*/){
            availablePairsInB1.V();
            emptyPairsInB1.P();
        }

        accessInsertingB1.V();
    }
}
