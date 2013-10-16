package pl.agh.student.mizmuda.lab1;

import java.util.ArrayList;

public class S implements Runnable {
    private final int packageSize = 2;

    private final Semaphore availableInProduction;
    private final Semaphore spaceInProduction;
    private final Semaphore availableInRelease;
    private final Semaphore spaceInRelease;
    private MutableInteger releaseInsertIndex;
    private MutableInteger productionDeleteIndex;
    @NotNeededIfSingleInstanceOf(entity = "S")
    private final BinarySemaphore insertingToRelease;
    @NotNeededIfSingleInstanceOf(entity = "S")
    private final BinarySemaphore deletingFromProduction;
    private final Product[] production;
    private final Product[] release;

    public S(Semaphore availableInProduction, Semaphore spaceInProduction, Semaphore availableInRelease,
             Semaphore spaceInRelease, MutableInteger releaseInsertIndex, MutableInteger productionDeleteIndex,
             BinarySemaphore insertingToRelease, BinarySemaphore deletingFromProduction, Product[] production,
             Product[] release) {
        this.availableInProduction = availableInProduction;
        this.spaceInProduction = spaceInProduction;
        this.availableInRelease = availableInRelease;
        this.spaceInRelease = spaceInRelease;
        this.releaseInsertIndex = releaseInsertIndex;
        this.productionDeleteIndex = productionDeleteIndex;
        this.insertingToRelease = insertingToRelease;
        this.deletingFromProduction = deletingFromProduction;
        this.production = production;
        this.release = release;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < packageSize; i++) {
                availableInProduction.P();
            }
            System.out.println("S reserved " + packageSize + " products");
            for (int i = 0; i < packageSize; i++) {
                spaceInRelease.P();
            }
            System.out.println("S reserved " + packageSize + " places");
            deletingFromProduction.P();
            insertingToRelease.P();
            for (int i = 0; i < packageSize; i++) {
                release[releaseInsertIndex.value] = production[productionDeleteIndex.value];
                production[productionDeleteIndex.value] = null;
                releaseInsertIndex.incrementModulo(release.length);
                productionDeleteIndex.incrementModulo(production.length);
            }
            insertingToRelease.V();
            deletingFromProduction.V();
            for (int i = 0; i < packageSize; i++) {
                availableInRelease.V();
            }
            System.out.println("S moved " + packageSize + " products");
            for (int i = 0; i < packageSize; i++) {
                spaceInProduction.V();
            }
            System.out.println("S released space for " + packageSize + " products");
        }
    }
}
