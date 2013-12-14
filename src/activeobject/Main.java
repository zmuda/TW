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

public class Main {
    public static int probeSize = TaskDurations.probeSize;
    public static int entitiesCount = 10;
    public static long totalMonitorTime;
    public static long totalActiveObjectTime;
    public static long totalSideTasks;
    public static int workersPoolSize = 4;
    public static int bufferSize = 30;

    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {

        StringBuilder builder = new StringBuilder("Entities\tActiveObject\tMonitor\tSideTasksActiveObject\tSideTasksMonitor\tTasksActiveObject\tTasksMonitor\n");

        while (probeSize < 40) {

            launch();

            double taskTotal = totalActiveObjectTime / 2 / probeSize / entitiesCount / 1000;
            long sideTasks = totalSideTasks / 2 / entitiesCount;
            double monitorTaskTotal = totalMonitorTime / 2 / probeSize / entitiesCount / 1000;
            long monitorSideTasks = probeSize;


            builder.append(entitiesCount + "\t");
            builder.append(taskTotal + "\t");
            builder.append(monitorTaskTotal + "\t");
            builder.append(sideTasks + "\t");
            builder.append(monitorSideTasks + "\t");
            builder.append(probeSize + "\t");
            builder.append(probeSize + "\n");

            probeSize += 30;
        }
        System.out.print(builder);
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.currentTimeMillis() + "_log.log"));
        writer.write(builder.toString());
        writer.close();
    }

    public static void launch() throws InterruptedException, ExecutionException {
        totalMonitorTime = -System.nanoTime();
        launchMonitorSolution();
        totalMonitorTime += System.nanoTime();
        totalActiveObjectTime = -System.nanoTime();
        totalSideTasks = 0;
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
            callables.add(new activeobject.activeproducersconsumers.Producer(service, bufferSize));
            callables.add(new activeobject.activeproducersconsumers.Consumer(service, bufferSize));

        }
        //logger.info("Producers: " + this.entities + "\tConsumers: " + this.entities + "\tBuffer for: " + bufferSize);
        ExecutorService activeObjectExecutor = Executors.newSingleThreadExecutor();
        activeObjectExecutor.submit(service);
        List<Future<Integer>> results = executorService.invokeAll(callables);

        for (Future<Integer> i : results) {
            totalSideTasks += i.get();
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
