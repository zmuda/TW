package pl.agh.student.mizmuda.lab1;

import java.util.logging.Logger;

public class S implements Runnable {
    private Logger logger = Logger.getLogger("lab1");
    private final int packageSize = 2;

    private final Semaphore availableInProduction;
    private final Semaphore spaceInProduction;
    private final Semaphore availableInRelease;
    private final Semaphore spaceInRelease;
    private MutableInteger releaseInsertIndex;
    private MutableInteger productionDeleteIndex;
    private final Product[] production;
    private final Product[] release;

    public S(Semaphore availableInProduction, Semaphore spaceInProduction, Semaphore availableInRelease,
             Semaphore spaceInRelease, MutableInteger releaseInsertIndex, MutableInteger productionDeleteIndex,
             Product[] production, Product[] release) {
        this.availableInProduction = availableInProduction;
        this.spaceInProduction = spaceInProduction;
        this.availableInRelease = availableInRelease;
        this.spaceInRelease = spaceInRelease;
        this.releaseInsertIndex = releaseInsertIndex;
        this.productionDeleteIndex = productionDeleteIndex;
        this.production = production;
        this.release = release;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < packageSize; i++) {
                availableInProduction.P();
            }
            logger.info("S reserved " + packageSize + " products");
            for (int i = 0; i < packageSize; i++) {
                spaceInRelease.P();
            }
            logger.info("S reserved " + packageSize + " places");
            logger.info(Main.buffersOccupationString(production) + "\t" + Main.buffersOccupationString(release));
            for (int i = 0; i < packageSize; i++) {
                release[releaseInsertIndex.value] = production[productionDeleteIndex.value];
                production[productionDeleteIndex.value] = null;
                releaseInsertIndex.incrementModulo(release.length);
                productionDeleteIndex.incrementModulo(production.length);
            }
            for (int i = 0; i < packageSize; i++) {
                availableInRelease.V();
            }
            logger.info("S moved " + packageSize + " products");
            logger.info(Main.buffersOccupationString(production) + "\t" + Main.buffersOccupationString(release));
            for (int i = 0; i < packageSize; i++) {
                spaceInProduction.V();
            }
            logger.info("S released space for " + packageSize + " products");
        }
    }
}
