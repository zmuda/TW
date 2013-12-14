package activeobject;

import activeobject.activeproducersconsumers.core.ProducersConsumersService;
import activeobject.mointoractiveobject.Buffer;
import activeobject.mointoractiveobject.Consumer;
import activeobject.mointoractiveobject.IBuffer;
import activeobject.mointoractiveobject.Producer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Launcher {
    private final int entities;
    private final int bufferSize;

    public Launcher(int entities, int bufferSize) {
        this.entities = entities;
        this.bufferSize = bufferSize;
    }

    private void launchMonitorSolution() throws InterruptedException {
        IBuffer buffer = new Buffer(bufferSize);
        ExecutorService service = Executors.newFixedThreadPool(TaskAbstractionAndStats.workersPoolSize);

        for (int i = 0; i < entities; i++) {
            service.submit(new Producer(buffer));
            service.submit(new Consumer(buffer));
        }

        service.shutdown();
        while (!service.isTerminated()) {
            service.awaitTermination(10, TimeUnit.MILLISECONDS);
        }
    }

    private void launchActiveObjectSolution() throws InterruptedException, ExecutionException {
        Logger logger = Logger.getLogger("activeobject - losowa ilosc");
        Random random = new Random(System.currentTimeMillis());

        BasicConfigurator.configure();

        ExecutorService executorService = Executors.newFixedThreadPool(TaskAbstractionAndStats.workersPoolSize);
        ProducersConsumersService<Integer> service = new ProducersConsumersService<Integer>(bufferSize, 2);
        Collection<Callable<Integer>> entities = new LinkedList<Callable<Integer>>();

        for (int i = 0; i < this.entities; i++) {
            entities.add(new activeobject.activeproducersconsumers.Producer(service, random, bufferSize));
            entities.add(new activeobject.activeproducersconsumers.Consumer(service, random, bufferSize));

        }
        logger.info("Producers: " + this.entities + "\tConsumers: " + this.entities + "\tBuffer for: " + bufferSize);
        ExecutorService activeObjectExecutor = Executors.newSingleThreadExecutor();
        activeObjectExecutor.submit(service);
        List<Future<Integer>> results = executorService.invokeAll(entities);

        for (Future<Integer> i : results) {
            TaskAbstractionAndStats.totalSideTasks += i.get();
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            executorService.awaitTermination(10, TimeUnit.MILLISECONDS);
        }

        service.shutdown();
        activeObjectExecutor.shutdown();
        while (!activeObjectExecutor.isTerminated()) {
            activeObjectExecutor.awaitTermination(50, TimeUnit.MILLISECONDS);
        }
    }

    public void launch() throws InterruptedException, ExecutionException {
        TaskAbstractionAndStats.totalMonitorTime = -System.nanoTime();
        launchMonitorSolution();
        TaskAbstractionAndStats.totalMonitorTime += System.nanoTime();
        TaskAbstractionAndStats.totalActiveObjectTime = -System.nanoTime();
        TaskAbstractionAndStats.totalSideTasks = 0;
        launchActiveObjectSolution();
        TaskAbstractionAndStats.totalActiveObjectTime += System.nanoTime();
    }
}
