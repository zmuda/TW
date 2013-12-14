package activeobject.activeproducersconsumers;

import activeobject.TaskDurations;
import activeobject.activeproducersconsumers.core.ProducersConsumersService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer implements Callable<Integer> {

    private ProducersConsumersService<Integer> service;

    public Producer(ProducersConsumersService<Integer> service) {
        this.service = service;
    }

    @Override
    public Integer call() throws Exception {
        int i = 0;
        int sideTasksCount = 0;
        try {
            while (i < TaskDurations.probeSize && !Thread.currentThread().isInterrupted()) {
                Future<Integer> future = service.produce();
                while (!future.isDone()) {
                    TaskDurations.waitForSideTaskToComplete();
                    sideTasksCount++;
                }
                future.get();
                i++;
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
