package activeobject;

import java.math.BigInteger;

public class TaskAbstractionAndStats {
    public static int probeSize = 1;
    public static int entitiesCount = 10;
    public static long totalMonitorTime;
    public static long totalActiveObjectTime;
    public static long totalSideTasks;
    public static int workersPoolSize = 4;

    private static void countdown(BigInteger number) {
        while (number.signum() > 0) {
            number = number.subtract(BigInteger.ONE);
        }
    }

    public static void waitForSideTaskToComplete() throws InterruptedException {
        countdown(BigInteger.valueOf(12345));
    }

    public static void waitForItemToProduce() throws InterruptedException {
        countdown(BigInteger.valueOf(12345));
    }

    public static void waitForItemToConsume() throws InterruptedException {
        countdown(BigInteger.valueOf(12345));
    }


}
