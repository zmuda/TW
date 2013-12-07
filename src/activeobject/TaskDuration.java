package activeobject;

import java.util.Random;

public class TaskDuration {
    private final static Random random = new Random(System.currentTimeMillis());

    public static void waitForItemToProduce() throws InterruptedException {
        Thread.sleep(random.nextInt(100));
    }

    public static void waitForItemToConsume() throws InterruptedException {
        Thread.sleep(random.nextInt(100));
    }
}
