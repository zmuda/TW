package pl.agh.student.mizmuda.lab2.multimonitor;

import org.apache.log4j.BasicConfigurator;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        int limitA = 3;
        int limitB = 2;
        final DuplexMonitor lock = new DuplexMonitor(limitA, limitB);
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
                        Integer a = lock.lockA();
                        try {
                            Thread.sleep(random.nextInt(200) + 11);
                        } catch (InterruptedException e) {
                        }
                        lock.unlockA(a);
                    }
                }
            });
        }
        for (int i = 0; i < lockingA; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Integer b = lock.lockB();
                        try {
                            Thread.sleep(random.nextInt(200) + 11);
                        } catch (InterruptedException e) {
                        }
                        lock.unlockB(b);
                    }
                }
            });
        }
        for (int i = 0; i < lockingA; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        ArrayList<Integer> anb = lock.lockAnB();
                        try {
                            Thread.sleep(random.nextInt(100) + 11);
                        } catch (InterruptedException e) {
                        }
                        lock.unlockPartA(anb.get(0));
                        try {
                            Thread.sleep(random.nextInt(100) + 11);
                        } catch (InterruptedException e) {
                        }
                        lock.unlockPartB(anb.get(1));
                    }
                }
            });
        }
    }
}
