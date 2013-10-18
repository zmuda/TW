package pl.agh.student.mizmuda.lab1;

public class K implements Runnable {
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
            System.out.println("K reserved " + packageSize + " products");
            System.out.println("\t" + Main.buffersOccupationString(release));
            for (int i = 0; i < packageSize; i++) {
                Product.destroyInstance(release[releaseDeleteIndex.value]);
                release[releaseDeleteIndex.value] = null;
                releaseDeleteIndex.incrementModulo(release.length);
            }
            System.out.println("\t" + Main.buffersOccupationString(release));
            System.out.println("K consumed " + packageSize + " products");
            for (int i = 0; i < packageSize; i++) {
                spaceInRelease.V();
            }
            System.out.println("K released space for " + packageSize + " products");
        }
    }
}
