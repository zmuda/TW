package pl.agh.student.mizmuda.lab2.multimonitor;

import org.apache.log4j.BasicConfigurator;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        final DuplexMonitor lock = new DuplexMonitor();
        final Random random = new Random();
        int lockingA = 3;
        int lockingB = 3;
        int lockingAnB = 3;
        ExecutorService service = Executors.newFixedThreadPool(lockingA + lockingB + lockingAnB);
        for (int i = 0; i < lockingA; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        lock.lockA();
                        try {
                            Thread.sleep(random.nextInt(200) + 11);
                        } catch (InterruptedException e) {
                        }
                        lock.unlockA();
                    }
                }
            });
        }
        for (int i = 0; i < lockingA; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        lock.lockB();
                        try {
                            Thread.sleep(random.nextInt(200) + 11);
                        } catch (InterruptedException e) {
                        }
                        lock.unlockB();
                    }
                }
            });
        }
        for (int i = 0; i < lockingA; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        lock.lockAnB();
                        try {
                            Thread.sleep(random.nextInt(100) + 11);
                        } catch (InterruptedException e) {
                        }
                        lock.unlockPartA();
                        try {
                            Thread.sleep(random.nextInt(100) + 11);
                        } catch (InterruptedException e) {
                        }
                        lock.unlockPartB();
                    }
                }
            });
        }
    }
}
