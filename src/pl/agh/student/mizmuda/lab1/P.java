package pl.agh.student.mizmuda.lab1;

import java.util.ArrayList;

public class P implements Runnable {

    private final BinarySemaphore insertingToProduction;
    private final Semaphore availableInProduction;
    private final Semaphore spaceInProduction;
    private Integer productionInsertIndex;
    private final Product[] production;
    private final String id;

    public P(BinarySemaphore insertingToProduction, Semaphore availableInProduction, Semaphore spaceInProduction,
             Integer productionInsertIndex, Product[] production, String id) {
        this.insertingToProduction = insertingToProduction;
        this.availableInProduction = availableInProduction;
        this.spaceInProduction = spaceInProduction;
        this.productionInsertIndex = productionInsertIndex;
        this.production = production;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            Product product = Product.newInstance();
            System.out.println(id + " has ready product");
            spaceInProduction.P();
            System.out.println(id + " reserved space for product");
            insertingToProduction.P();
            production[productionInsertIndex] = product;
            productionInsertIndex++;
            productionInsertIndex %= production.length;
            System.out.println(id + " passed product");
            insertingToProduction.V();
            availableInProduction.V();
        }
    }
}
