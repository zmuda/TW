package activeobject;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {

        StringBuilder builder = new StringBuilder("Entities\tActiveObject\tMonitor\tSideTasksActiveObject\tSideTasksMonitor\tTasksActiveObject\tTasksMonitor\n");

        while (TaskAbstractionAndStats.probeSize < 40) {
            int entitiesCount = TaskAbstractionAndStats.entitiesCount;
            int bufferSize = 100;

            Launcher launcher = new Launcher(entitiesCount, bufferSize);

            launcher.launch();
            double taskTotal = TaskAbstractionAndStats.totalActiveObjectTime / 2 / TaskAbstractionAndStats.probeSize / entitiesCount / 1000;
            long sideTasks = TaskAbstractionAndStats.totalSideTasks / 2 / entitiesCount;
            double monitorTaskTotal = TaskAbstractionAndStats.totalMonitorTime / 2 / TaskAbstractionAndStats.probeSize / entitiesCount / 1000;
            long monitorSideTasks = TaskAbstractionAndStats.probeSize;


            builder.append(entitiesCount + "\t");
            builder.append(taskTotal + "\t");
            builder.append(monitorTaskTotal + "\t");
            builder.append(sideTasks + "\t");
            builder.append(monitorSideTasks + "\t");
            builder.append(TaskAbstractionAndStats.probeSize + "\t");
            builder.append(TaskAbstractionAndStats.probeSize + "\n");

            TaskAbstractionAndStats.probeSize += 30;
        }
        System.out.print(builder);
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.currentTimeMillis() + "_log.log"));
        writer.write(builder.toString());
        writer.close();
    }

}
