package activeobject;

import java.math.BigInteger;

public class TaskDurations {
    public static int probeSize = 10;
    public static int sideTaskSize = 12345;
    public static int mainTaskSize = 12345;

    private static void countdown(BigInteger number) {
        while (number.signum() > 0) {
            number = number.subtract(BigInteger.ONE);
        }
    }

    public static void waitForSideTaskToComplete() throws InterruptedException {
        countdown(BigInteger.valueOf(sideTaskSize));
    }

    public static void waitForItemToProduce() throws InterruptedException {
        countdown(BigInteger.valueOf(mainTaskSize));
    }

    public static void waitForItemToConsume() throws InterruptedException {
        countdown(BigInteger.valueOf(mainTaskSize));
    }


}
