package pl.agh.student.mizmuda.lab1;

public class Product {
    public static Product newInstance() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            //om nom nom - exception eaten
        }
        return new Product();
    }

    public static void destroyInstance(Product product) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            //om nom nom - exception eaten
        }
    }
}
