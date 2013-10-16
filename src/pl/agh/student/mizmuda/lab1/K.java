package pl.agh.student.mizmuda.lab1;

import java.util.ArrayList;

public class K implements Runnable {
    private final int packageSize;

    private final Semaphore availableInRelease;
    private final Semaphore spaceInRelease;
    private Integer releaseDeleteIndex;
    @NotNeededIfSingleInstanceOf(entity = "K")
    private final BinarySemaphore deletingFromRelease;
    private final ArrayList<Product> release;

    public K(Integer releaseDeleteIndex, Semaphore availableInRelease, Semaphore spaceInRelease,
             BinarySemaphore deletingFromRelease, ArrayList<Product> release) {
        this.releaseDeleteIndex = releaseDeleteIndex;
        this.availableInRelease = availableInRelease;
        this.spaceInRelease = spaceInRelease;
        this.deletingFromRelease = deletingFromRelease;
        this.release = release;

        this.packageSize=release.size();
    }

    @Override
    public void run() {
        for(int i=0;i<packageSize;i++){
            availableInRelease.P();
        }
        System.out.println("K reserved "+packageSize+" products");
        deletingFromRelease.P();
        for(int i=0;i<packageSize;i++){
            release.set(releaseDeleteIndex,null);
            releaseDeleteIndex++;
            releaseDeleteIndex%=release.size();
        }
        deletingFromRelease.V();
        System.out.println("K consumed "+packageSize+" products");
        for(int i=0;i<packageSize;i++){
            spaceInRelease.V();
        }
        System.out.println("K released space for "+packageSize+" products");
    }
}
