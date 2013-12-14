package activeobject.activeproducersconsumers;

import activeobject.TaskAbstractionAndStats;
import activeobject.activeproducersconsumers.core.ProducersConsumersService;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer implements Callable<Integer> {

    private ProducersConsumersService<Integer> service;
    private Random random;
    private int bufferLimit;

    public Producer(ProducersConsumersService<Integer> service, Random random, int bufferLimit) {
        this.service = service;
        this.random = random;
        this.bufferLimit = bufferLimit;
    }

    @Override
    public Integer call() throws Exception {
        int i = 0;
        int sideTasksCount = 0;
        try {
            while (i < TaskAbstractionAndStats.probeSize && !Thread.currentThread().isInterrupted()) {
                Future<Integer> future = service.produce(1);
                while (!future.isDone()) {
                    TaskAbstractionAndStats.waitForSideTaskToComplete();
                    sideTasksCount++;
                }
                future.get();
                i++;
                TaskAbstractionAndStats.waitForSideTaskToComplete();
                sideTasksCount++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return sideTasksCount;
        }

    }
}
