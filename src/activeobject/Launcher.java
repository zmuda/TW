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
    private final LongCollector consumerMonitorExecutionTimes;
    private final LongCollector producerMonitorExecutionTimes;
    private final LongCollector producerIdleTimes;
    private final LongCollector consumerIdleTimes;
    private final LongCollector producerExecutionTimes;
    private final LongCollector consumerExecutionTimes;
    private final LongCollector activeObjectExecutionTime;

    public Launcher(int producers, int consumers, int bufferSize, LongCollector consumerMonitorExecutionTimes,
                    LongCollector producerMonitorExecutionTimes, LongCollector producerIdleTimes,
                    LongCollector consumerIdleTimes, LongCollector producerExecutionTimes,
                    LongCollector consumerExecutionTimes, LongCollector activeObjectExecutionTime) {
        this.producers = producers;
        this.consumers = consumers;
        this.bufferSize = bufferSize;
        this.consumerMonitorExecutionTimes = consumerMonitorExecutionTimes;
        this.producerMonitorExecutionTimes = producerMonitorExecutionTimes;
        this.producerIdleTimes = producerIdleTimes;
        this.consumerIdleTimes = consumerIdleTimes;
        this.producerExecutionTimes = producerExecutionTimes;
        this.consumerExecutionTimes = consumerExecutionTimes;
        this.activeObjectExecutionTime = activeObjectExecutionTime;
    }

    private void launchMonitorSolution() throws InterruptedException {
        IBuffer buffer = new Buffer(bufferSize);
        ExecutorService service = Executors.newFixedThreadPool(producers + consumers);

        for (int i = 0; i < producers; i++) {
            service.submit(new Producer(buffer, producerMonitorExecutionTimes));
        }
        for (int i = 0; i < consumers; i++) {
            service.submit(new Consumer(buffer, consumerMonitorExecutionTimes));
        }

        service.shutdown();
        while (!service.isTerminated()) {
            service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        }

        System.err.println("\tMean consumer execution time: " + consumerMonitorExecutionTimes.getMean() / TaskDuration.probeSize);
        System.err.println("\tMean producer execution time: " + producerMonitorExecutionTimes.getMean() / TaskDuration.probeSize);

    }

    private void launchActiveObjectSolution() throws InterruptedException {
        Logger logger = Logger.getLogger("activeobject - losowa ilosc");
        Random random = new Random(System.currentTimeMillis());

        BasicConfigurator.configure();

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

    public void launch() throws InterruptedException {
        launchMonitorSolution();
        launchActiveObjectSolution();
    }
}
