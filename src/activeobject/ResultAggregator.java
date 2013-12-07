package activeobject;

import activeobject.activeproducersconsumers.problemspecific.ProducersConsumersService;
import activeobject.mointoractiveobject.Buffer;
import activeobject.mointoractiveobject.Consumer;
import activeobject.mointoractiveobject.IBuffer;
import activeobject.mointoractiveobject.Producer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResultAggregator {
    private static int producers = 2;
    private static int consumers = 1;
    private static int bufferSize = 12;

    private static void launchMonitorSolution() {
        IBuffer buffer = new Buffer(bufferSize);
        ExecutorService service = Executors.newFixedThreadPool(producers + consumers);
        for (int i = 0; i < producers; i++) {
            service.submit(new Producer(buffer));
        }
        for (int i = 0; i < consumers; i++) {
            service.submit(new Consumer(buffer));
        }
    }

    private static void launchActiveObjectSolution() {
        Logger logger = Logger.getLogger("activeobject - losowa ilosc");
        Random random = new Random(System.currentTimeMillis());

        BasicConfigurator.configure();
        ExecutorService executorService = Executors.newFixedThreadPool(producers + consumers);
        ProducersConsumersService<Integer> service = new ProducersConsumersService<Integer>(bufferSize, 2);
        for (int i = 0; i < producers; i++) {
            executorService.submit(new activeobject.activeproducersconsumers.Producer(service, random, bufferSize));
        }
        for (int i = 0; i < consumers; i++) {
            executorService.submit(new activeobject.activeproducersconsumers.Consumer(service, random, bufferSize));
        }
        logger.info("Producers: " + producers + "\tConsumers: " + consumers + "\tBuffer for: " + bufferSize);
        service.start();
    }

    public static void main(String[] args) {
        //launchMonitorSolution();
        launchActiveObjectSolution();
    }
}