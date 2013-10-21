package pl.agh.student.mizmuda.lab1;

import java.util.logging.Logger;

public class P implements Runnable {
    private Logger logger = Logger.getLogger("lab1");

    private final BinarySemaphore insertingToProduction;
    private final Semaphore availableInProduction;
    private final Semaphore spaceInProduction;
    private MutableInteger productionInsertIndex;
    private final Product[] production;
    private final String id;

    public P(BinarySemaphore insertingToProduction, Semaphore availableInProduction, Semaphore spaceInProduction,
             MutableInteger productionInsertIndex, Product[] production, String id) {
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
            logger.info(id + " has ready product");
            spaceInProduction.P();
            logger.info(id + " reserved space for product");
            logger.info(Main.buffersOccupationString(production));
            insertingToProduction.P();
            production[productionInsertIndex.value] = product;
            productionInsertIndex.incrementModulo(production.length);
            logger.info(Main.buffersOccupationString(production));
            logger.info(id + " passed product");
            insertingToProduction.V();
            availableInProduction.V();
        }
    }
}
