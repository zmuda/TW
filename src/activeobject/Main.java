package activeobject;


import activeobject.activeproducersconsumers.core.ProducersConsumersService;
import activeobject.mointoractiveobject.Buffer;
import activeobject.mointoractiveobject.Consumer;
import activeobject.mointoractiveobject.IBuffer;
import activeobject.mointoractiveobject.Producer;
import org.apache.log4j.BasicConfigurator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static activeobject.TaskDurations.probeSize;

public class Main {
    public static int minEntitiesCount = 1;
    public static int entitiesCountStep = 9;
    public static int maxEntitiesCount = 37;
    public static int minProbeSize = 1;
    public static int probeSizeStep = 37;
    public static int maxProbeSize = 190;
    public static int entitiesCount = 10;
    public static long totalMonitorTime;
    public static long totalActiveObjectTime;
    public static int activeObjectSideTasksCount;
    public static int workersPoolSize = 4;
    public static int bufferSize = 30;

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
        changingProbeSize();
        changingEntitiesCount();
    }

    public static void changingProbeSize() throws InterruptedException, IOException, ExecutionException {
        StringBuilder builder = new StringBuilder("Entities\tmain tasks per entity\tActiveObject total time (ms)" +
                "\tMonitor total time (ms)\tActiveObject side tasks mean\tMonitor side tasks mean\n");
        entitiesCount = 10;
        for (probeSize = minProbeSize; probeSize < maxProbeSize; probeSize += probeSizeStep) {
            launch();
            appendResults(builder);
            System.out.print(builder);
        }
        System.out.print(builder);
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.currentTimeMillis() + "_probeSize.log"));
        writer.write(builder.toString());
        writer.close();
    }

    public static void changingEntitiesCount() throws InterruptedException, IOException, ExecutionException {
        probeSize = 30;
        StringBuilder builder = new StringBuilder("Entities\tmain tasks per entity\tActiveObject total time (ms)" +
                "\tMonitor total time (ms)\tActiveObject side tasks mean\tMonitor side tasks mean\n");
        for (entitiesCount = minEntitiesCount; entitiesCount < maxEntitiesCount; entitiesCount += entitiesCountStep) {
            launch();
            appendResults(builder);
            System.out.print(builder);
        }
        System.out.print(builder);
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.currentTimeMillis() + "_entitiesCount.log"));
        writer.write(builder.toString());
        writer.close();
    }

    private static void appendResults(StringBuilder builder) {
        int entities = entitiesCount;
        int meanMainTasks = probeSize;
        double meanActiveObjectSideTasks = activeObjectSideTasksCount / 2 / entities;
        int meanMonitorSideTasks = probeSize;

        builder.append(entities + "\t");
        builder.append(meanMainTasks + "\t");
        builder.append(totalActiveObjectTime / 1000 / 1000 + "\t");
        builder.append(totalMonitorTime / 1000 / 1000 + "\t");
        builder.append(meanActiveObjectSideTasks + "\t");
        builder.append(meanMonitorSideTasks + "\n");
    }

    public static void launch() throws InterruptedException, ExecutionException {
        totalMonitorTime = -System.nanoTime();
        launchMonitorSolution();
        totalMonitorTime += System.nanoTime();
        totalActiveObjectTime = -System.nanoTime();
        activeObjectSideTasksCount = 0;
        launchActiveObjectSolution();
        totalActiveObjectTime += System.nanoTime();
    }

    private static void launchMonitorSolution() throws InterruptedException {
        IBuffer buffer = new Buffer(bufferSize);
        ExecutorService service = Executors.newFixedThreadPool(workersPoolSize);

        for (int i = 0; i < entitiesCount; i++) {
            service.submit(new Producer(buffer));
            service.submit(new Consumer(buffer));
        }

        shutdownGracefully(service);
    }

    private static void launchActiveObjectSolution() throws InterruptedException, ExecutionException {
        //Logger logger = Logger.getLogger("activeobject - losowa ilosc");
        Random random = new Random(System.currentTimeMillis());

        BasicConfigurator.configure();

        ExecutorService executorService = Executors.newFixedThreadPool(workersPoolSize);
        ProducersConsumersService<Integer> service = new ProducersConsumersService<Integer>(bufferSize, 2);
        Collection<Callable<Integer>> callables = new LinkedList<Callable<Integer>>();

        for (int i = 0; i < entitiesCount; i++) {
            callables.add(new activeobject.activeproducersconsumers.Producer(service));
            callables.add(new activeobject.activeproducersconsumers.Consumer(service));

        }
        //logger.info("Producers: " + this.entities + "\tConsumers: " + this.entities + "\tBuffer for: " + bufferSize);
        ExecutorService activeObjectExecutor = Executors.newSingleThreadExecutor();
        activeObjectExecutor.submit(service);
        List<Future<Integer>> results = executorService.invokeAll(callables);

        for (Future<Integer> i : results) {
            activeObjectSideTasksCount += i.get();
        }

        shutdownGracefully(executorService);
        service.shutdown();
        shutdownGracefully(activeObjectExecutor);
    }

    private static void shutdownGracefully(ExecutorService executorService) throws InterruptedException {
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            executorService.awaitTermination(50, TimeUnit.MILLISECONDS);
        }
    }

}
