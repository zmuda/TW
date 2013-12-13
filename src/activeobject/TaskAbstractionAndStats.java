package activeobject;

import java.math.BigInteger;

public class TaskAbstractionAndStats {
    public static int probeSize = 5;
    public static long totalMonitorTime;
    public static long totalActiveObjectTime;
    public static long totalSideTasks;

    private static void countdown(BigInteger number) {
        BigInteger initial = number;
        while (number.signum() > 0) {
            number = number.subtract(BigInteger.ONE);
        }
    }

    public static void waitForSideTaskToComplete() throws InterruptedException {
        countdown(BigInteger.valueOf(123456));
    }

    public static void waitForItemToProduce() throws InterruptedException {
        countdown(BigInteger.valueOf(123456));
    }

    public static void waitForItemToConsume() throws InterruptedException {
        countdown(BigInteger.valueOf(123456));
    }


}
