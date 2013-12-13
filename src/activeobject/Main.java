package activeobject;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        int entitiesCount = 10;
        int bufferSize = 100;

        StringBuilder builder = new StringBuilder("Entities\tIdle\tBusy\tMonitor\n");

        while (entitiesCount < 200) {
            Launcher launcher = new Launcher(entitiesCount, entitiesCount, bufferSize);

            launcher.launch();

            entitiesCount += 20;
        }
        System.out.print(builder);
        BufferedWriter writer = new BufferedWriter(new FileWriter(System.currentTimeMillis() + "_log.log"));
        writer.write(builder.toString());
        writer.close();
    }

}
