package pl.agh.student.mizmuda.lab1;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int productionSize = 12;
    private static final int releaseSize = 12;
    private static final int countOfP=6;

    private static final Semaphore availableInProduction = new Semaphore(0);
    private static final Semaphore spaceInProduction = new Semaphore(productionSize);
    private static final Semaphore availableInRelease = new Semaphore(0);
    private static final Semaphore spaceInRelease = new Semaphore(releaseSize);
    private static MutableInteger releaseInsertIndex = new MutableInteger();
    private static MutableInteger productionDeleteIndex = new MutableInteger();
    private static MutableInteger releaseDeleteIndex = new MutableInteger();
    private static MutableInteger productionInsertIndex = new MutableInteger();
    private static final Product[] production = new Product[productionSize];
    private static final Product[] release = new Product[releaseSize];

    @NotNeededIfSingleInstanceOf(entity = "K")
    private static final BinarySemaphore deletingFromRelease = new BinarySemaphore(true);
    @NotNeededIfSingleInstanceOf(entity = "S")
    private static final BinarySemaphore insertingToRelease = new BinarySemaphore(true);
    @NotNeededIfSingleInstanceOf(entity = "S")
    private static final BinarySemaphore deletingFromProduction = new BinarySemaphore(true);
    private static final BinarySemaphore insertingToProduction = new BinarySemaphore(true);


    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(countOfP+1+1);
        service.submit(new K(releaseDeleteIndex,availableInRelease,spaceInRelease,deletingFromRelease,release));
        service.submit(new S(availableInProduction,spaceInProduction,availableInRelease,spaceInRelease,
                releaseInsertIndex,productionDeleteIndex,insertingToRelease,deletingFromProduction,
                production,release));
        for(int i=0;i<countOfP;i++){
            service.submit(new P(insertingToProduction,availableInProduction,spaceInProduction,productionInsertIndex,
                    production,"P"+String.valueOf(i)));
        }
        while(true){
            try {
                //this is breakpoint placeholder
                Thread.sleep(200);
            } catch (InterruptedException e) {}
        }
    }
}
