package pl.agh.student.mizmuda.activeobject.producersconsumers;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import pl.agh.student.mizmuda.activeobject.core.ISubmittableExecutor;
import pl.agh.student.mizmuda.activeobject.core.SubmittableExecutor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private Logger logger = Logger.getLogger("ao");


    public static void main(String[] args) {
        BasicConfigurator.configure();
        int prods = 50;
        int cons = 50;
        int bufferSize = 30;
        Queue<Integer> buffer = new LinkedList<Integer>();
        ISubmittableExecutor executor = new SubmittableExecutor();
        ExecutorService service = Executors.newFixedThreadPool(prods + cons);
        for (int i = 0; i < prods; i++) {
            service.submit(new Producer(executor, buffer, bufferSize));
        }
        for (int i = 0; i < cons; i++) {
            service.submit(new Consumer(executor, buffer));
        }
        executor.getWorker().run();
    }
}
