package activeobject;

import activeobject.activeproducersconsumers.core.ProducersConsumersService;
import activeobject.mointoractiveobject.Buffer;
import activeobject.mointoractiveobject.Consumer;
import activeobject.mointoractiveobject.IBuffer;
import activeobject.mointoractiveobject.Producer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Launcher {
    private final int producers;
    private final int consumers;
    private final int bufferSize;

    public Launcher(int producers, int consumers, int bufferSize) {
        this.producers = producers;
        this.consumers = consumers;
        this.bufferSize = bufferSize;
    }

    private void launchMonitorSolution() throws InterruptedException {
        IBuffer buffer = new Buffer(bufferSize);
        ExecutorService service = Executors.newFixedThreadPool(producers + consumers);

        for (int i = 0; i < producers; i++) {
            service.submit(new Producer(buffer));
        }
        for (int i = 0; i < consumers; i++) {
            service.submit(new Consumer(buffer));
        }

        service.shutdown();
        while (!service.isTerminated()) {
            service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        }
    }

    private void launchActiveObjectSolution() throws InterruptedException {
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
        ExecutorService activeObjectExecutor = Executors.newSingleThreadExecutor();
        activeObjectExecutor.submit(service);

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        }

        service.shutdown();
        activeObjectExecutor.shutdown();
        while (!activeObjectExecutor.isTerminated()) {
            activeObjectExecutor.awaitTermination(50, TimeUnit.MILLISECONDS);
        }
    }

    public void launch() throws InterruptedException {
        launchMonitorSolution();
        launchActiveObjectSolution();
    }
}
