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
import java.util.concurrent.TimeUnit;

public class Main {
    private static int producers = 2;
    private static int consumers = 1;
    private static int bufferSize = 12;

    private static void launchMonitorSolution() throws InterruptedException {
        IBuffer buffer = new Buffer(bufferSize);
        ExecutorService service = Executors.newFixedThreadPool(producers + consumers);

        LongCollecter consumerExecutionTimes = new LongCollecter();
        LongCollecter producerExecutionTimes = new LongCollecter();

        for (int i = 0; i < producers; i++) {
            service.submit(new Producer(buffer, producerExecutionTimes));
        }
        for (int i = 0; i < consumers; i++) {
            service.submit(new Consumer(buffer, consumerExecutionTimes));
        }

        service.shutdown();
        while (!service.isTerminated()) {
            service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        }

        System.err.println("\tMean consumer execution time: " + consumerExecutionTimes.getMean() / TaskDuration.probeSize);
        System.err.println("\tMean producer execution time: " + producerExecutionTimes.getMean() / TaskDuration.probeSize);

    }

    private static void launchActiveObjectSolution() throws InterruptedException {
        Logger logger = Logger.getLogger("activeobject - losowa ilosc");
        Random random = new Random(System.currentTimeMillis());

        BasicConfigurator.configure();

        LongCollecter consumerExecutionTimes = new LongCollecter();
        LongCollecter producerExecutionTimes = new LongCollecter();

        LongCollecter consumerIdleTimes = new LongCollecter();
        LongCollecter producerIdleTimes = new LongCollecter();

        LongCollecter activeObjectExecutionTime = new LongCollecter();

        ExecutorService executorService = Executors.newFixedThreadPool(producers + consumers);
        ProducersConsumersService<Integer> service = new ProducersConsumersService<Integer>(bufferSize, 2, activeObjectExecutionTime);

        for (int i = 0; i < producers; i++) {
            executorService.submit(new activeobject.activeproducersconsumers.Producer(service, random, bufferSize, producerExecutionTimes, producerIdleTimes));
        }
        for (int i = 0; i < consumers; i++) {
            executorService.submit(new activeobject.activeproducersconsumers.Consumer(service, random, bufferSize, consumerExecutionTimes, consumerIdleTimes));
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
        System.err.println("Mean execution time: " + activeObjectExecutionTime.getMean());

        System.err.println("Mean consumer idle time: " + consumerIdleTimes.getMean() / TaskDuration.probeSize);
        System.err.println("Mean consumer dispatch time: " + (consumerExecutionTimes.getMean() - consumerIdleTimes.getMean()) / TaskDuration.probeSize);

        System.err.println("Mean producer idle time: " + producerIdleTimes.getMean() / TaskDuration.probeSize);
        System.err.println("Mean producer dispatch time: " + (producerExecutionTimes.getMean() - producerIdleTimes.getMean()) / TaskDuration.probeSize);

    }

    public static void main(String[] args) throws InterruptedException {
        launchMonitorSolution();
        launchActiveObjectSolution();
    }
}
