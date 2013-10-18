package pl.agh.student.mizmuda.lab1;

public class P implements Runnable {

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
            System.out.println(id + " has ready product");
            spaceInProduction.P();
            System.out.println(id + " reserved space for product");
            System.out.println(Main.buffersOccupationString(production));
            insertingToProduction.P();
            production[productionInsertIndex.value] = product;
            productionInsertIndex.incrementModulo(production.length);
            System.out.println(Main.buffersOccupationString(production));
            System.out.println(id + " passed product");
            insertingToProduction.V();
            availableInProduction.V();
        }
    }
}
