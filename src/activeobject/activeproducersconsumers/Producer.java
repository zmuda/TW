package activeobject.activeproducersconsumers;

import activeobject.LongCollecter;
import activeobject.TaskDuration;
import activeobject.activeproducersconsumers.problemspecific.ProducersConsumersService;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer implements Runnable {

    private ProducersConsumersService<Integer> service;
    private Random random;
    private int bufferLimit;
    private final LongCollecter executionTimes;
    private final LongCollecter idleTimes;
    private long totalSpent = 0;
    private long notBusy = 0;

    public Producer(ProducersConsumersService<Integer> service, Random random, int bufferLimit, LongCollecter executionTimes, LongCollecter idleTimes) {
        this.service = service;
        this.random = random;
        this.bufferLimit = bufferLimit;
        this.executionTimes = executionTimes;
        this.idleTimes = idleTimes;
    }

    @Override
    public void run() {
        int i = 0;
        try {
            totalSpent = -System.currentTimeMillis();
            while (i < TaskDuration.probeSize && !Thread.currentThread().isInterrupted()) {
                Future<Integer> future = service.produce(1);
                notBusy -= System.currentTimeMillis();
                future.get();
                notBusy += System.currentTimeMillis();
                i++;
            }
            totalSpent += System.currentTimeMillis();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executionTimes.submit(totalSpent);
            idleTimes.submit(notBusy);
        }
    }
}
