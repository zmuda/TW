package pl.agh.student.mizmuda.lab1;

import java.util.logging.Logger;

public class K implements Runnable {
    private Logger logger = Logger.getLogger("lab1");
    private final int packageSize;

    private final Semaphore availableInRelease;
    private final Semaphore spaceInRelease;
    private MutableInteger releaseDeleteIndex;
    private final Product[] release;

    public K(MutableInteger releaseDeleteIndex, Semaphore availableInRelease, Semaphore spaceInRelease,
             Product[] release) {
        this.releaseDeleteIndex = releaseDeleteIndex;
        this.availableInRelease = availableInRelease;
        this.spaceInRelease = spaceInRelease;
        this.release = release;
        this.packageSize = release.length;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < packageSize; i++) {
                availableInRelease.P();
            }
            logger.info("K reserved " + packageSize + " products");
            logger.info("\t" + Main.buffersOccupationString(release));
            for (int i = 0; i < packageSize; i++) {
                Product.destroyInstance(release[releaseDeleteIndex.value]);
                release[releaseDeleteIndex.value] = null;
                releaseDeleteIndex.incrementModulo(release.length);
            }
            logger.info("\t" + Main.buffersOccupationString(release));
            logger.info("K consumed " + packageSize + " products");
            for (int i = 0; i < packageSize; i++) {
                spaceInRelease.V();
            }
            logger.info("K released space for " + packageSize + " products");
        }
    }
}
