package pl.agh.student.mizmuda.lab1.test;

import pl.agh.student.mizmuda.lab1.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test3 {
    private static final int productionSize = 12;
    private static final int releaseSize = 12;
    private static final int countOfP = 6;
    private static final Semaphore availableInProduction = new Semaphore(0);
    private static final Semaphore spaceInProduction = new Semaphore(productionSize);
    private static final Semaphore availableInRelease = new Semaphore(0);
    private static final Semaphore spaceInRelease = new Semaphore(releaseSize);
    private static MutableInteger releaseInsertIndex = new MutableInteger();
    private static MutableInteger productionDeleteIndex = new MutableInteger();
    private static MutableInteger releaseDeleteIndex = new MutableInteger();
    private static MutableInteger productionInsertIndex = new MutableInteger();
    private static final Product[] production = new Product[productionSize];
    private static final Product[] release = new Product[releaseSize];
    private static final BinarySemaphore insertingToProduction = new BinarySemaphore(true);

    public static String buffersOccupationString(Product[] buffer) {
        StringBuffer res = new StringBuffer();
        for (Product p : buffer) {
            res.append((p == null) ? "□" : "■");
        }
        return res.toString();
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(countOfP + 1 + 1);
        for (int i = 0; i < countOfP; i++) {
            service.submit(new P(insertingToProduction, availableInProduction, spaceInProduction, productionInsertIndex,
                    production, "P" + String.valueOf(i)));
        }
        service.submit(new S(availableInProduction, spaceInProduction, availableInRelease, spaceInRelease,
                releaseInsertIndex, productionDeleteIndex, production, release));
            /* debugger handle */
        boolean halt = true;
        while (halt) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
