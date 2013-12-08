package activeobject;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        int entitiesCount = 10;
        int bufferSize = 100;
        LongCollector consumerMonitorExecutionTimes = new LongCollector();
        LongCollector producerMonitorExecutionTimes = new LongCollector();
        LongCollector producerIdleTimes = new LongCollector();
        LongCollector consumerIdleTimes = new LongCollector();
        LongCollector producerExecutionTimes = new LongCollector();
        LongCollector consumerExecutionTimes = new LongCollector();
        LongCollector activeObjectExecutionTime = new LongCollector();

        StringBuilder builder = new StringBuilder("Entities\tIdle\tBusy\tMonitor\n");

        while (entitiesCount < 200) {
            consumerMonitorExecutionTimes.reset();
            producerMonitorExecutionTimes.reset();
            producerIdleTimes.reset();
            consumerIdleTimes.reset();
            producerExecutionTimes.reset();
            consumerExecutionTimes.reset();
            activeObjectExecutionTime.reset();
            Launcher launcher = new Launcher(entitiesCount, entitiesCount, bufferSize, consumerMonitorExecutionTimes,
                    producerMonitorExecutionTimes, producerIdleTimes, consumerIdleTimes, producerExecutionTimes,
                    consumerExecutionTimes, activeObjectExecutionTime);

            launcher.launch();
            double entityIdle = (consumerIdleTimes.getMean() + producerIdleTimes.getMean()) / 2 / TaskDuration.probeSize;
            double taskTotal = (consumerExecutionTimes.getMean() + producerExecutionTimes.getMean()) / 2 /
                    TaskDuration.probeSize + activeObjectExecutionTime.getMean();
            double taskBusy = taskTotal - entityIdle;
            double monitorTaskTotal = (consumerMonitorExecutionTimes.getMean() + producerMonitorExecutionTimes.getMean()) /
                    2 / TaskDuration.probeSize;


            builder.append((long) entitiesCount + "\t");
            builder.append((long) entityIdle + "\t");
            builder.append((long) taskBusy + "\t");
            builder.append((long) monitorTaskTotal + "\n");

            entitiesCount += 20;
        }
        System.out.print(builder);
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.currentTimeMillis() + "_log.log"));
        writer.write(builder.toString());
        writer.close();
    }

}
