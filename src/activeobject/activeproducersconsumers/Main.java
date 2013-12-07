package activeobject.activeproducersconsumers;


import activeobject.activeproducersconsumers.problemspecific.ProducersConsumersService;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static Logger logger = Logger.getLogger("activeobject - losowa ilosc");
    private static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        BasicConfigurator.configure();
        int prods = 1;
        int cons = 1;
        int bufferSize = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        ProducersConsumersService<Integer> service = new ProducersConsumersService<Integer>(bufferSize, 2);
        for (int i = 0; i < prods; i++) {
            executorService.submit(new Producer(service, random, bufferSize));
        }
        for (int i = 0; i < cons; i++) {
            executorService.submit(new Consumer(service, random, bufferSize));
        }
        logger.info("Producers: " + prods + "\tConsumers: " + cons + "\tBuffer for: " + bufferSize);
        service.run();
    }
}
