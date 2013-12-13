package activeobject;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
        int entitiesCount = 1;
        int bufferSize = 100;

        StringBuilder builder = new StringBuilder("Entities\tActiveObject\tMonitor\tSideTasksActiveObject\tSideTasksMonitor\tTasksActiveObject\tTasksMonitor\n");

        while (TaskAbstractionAndStats.probeSize < 71) {
            Launcher launcher = new Launcher(entitiesCount, entitiesCount, bufferSize);

            launcher.launch();
            double taskTotal = TaskAbstractionAndStats.totalActiveObjectTime / 2 / TaskAbstractionAndStats.probeSize / entitiesCount;
            long sideTasks = TaskAbstractionAndStats.totalSideTasks / 2 / TaskAbstractionAndStats.probeSize / entitiesCount;
            double monitorTaskTotal = TaskAbstractionAndStats.totalMonitorTime / 2 / TaskAbstractionAndStats.probeSize / entitiesCount;
            long monitorSideTasks = TaskAbstractionAndStats.probeSize;


            builder.append(entitiesCount + "\t");
            builder.append(taskTotal + "\t");
            builder.append(monitorTaskTotal + "\t");
            builder.append(sideTasks + "\t");
            builder.append(monitorSideTasks + "\t");
            builder.append(TaskAbstractionAndStats.probeSize + "\t");
            builder.append(TaskAbstractionAndStats.probeSize + "\n");

            TaskAbstractionAndStats.probeSize += 17;
        }
        System.out.print(builder);
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.currentTimeMillis() + "_log.log"));
        writer.write(builder.toString());
        writer.close();
    }

}
