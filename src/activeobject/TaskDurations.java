package activeobject;

import java.math.BigInteger;

public class TaskDurations {
    public static int probeSize = 10;

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
