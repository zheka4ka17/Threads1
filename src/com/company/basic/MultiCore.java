package com.company.basic;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;

import java.util.List;
import java.util.concurrent.*;

public class MultiCore {
    public static void main(String[] args) throws Exception {
        int numCores = Runtime.getRuntime().availableProcessors();
        System.out.println(numCores);

        //Multi core vs single core

        //Start clock
        Instant now = Instant.now();
        ExecutorService executorService = Executors.newWorkStealingPool();
        List<Future<Double>> futures = new LinkedList<>(); //Список потоков

        int numWorkers = 4;
        for (int i = 0; i < numWorkers; i++)
            futures.add(i, executorService.submit(new Worker(i)));
            double avg = 0.;

            for(int i=0; i<numWorkers; i++)
                avg+=futures.get(i).get();
            avg = avg/numWorkers;

            Duration duration = Duration.between(now, Instant.now());
          System.out.printf(" Average: %2.3f, duration: %s%n", avg , duration);

        //Single core
        now = Instant.now();
        for (int i = 0; i < numWorkers; i++){
            Worker worker = new Worker(i);
            avg+=worker.call();
        }
        avg = avg / numWorkers;
        duration = Duration.between(now, Instant.now());
        System.out.printf(" Average: %2.3f, duration: %s%n", avg, duration);
    }


    static class Worker implements Callable<Double> {
        int id;
        long count = 2 << 20; //Количество добавляемых данных
        List<Double> data = new ArrayList<>();

        public Worker(int id) {

            this.id = id;
        }

        @Override
        public Double call() throws Exception {
            for (long i = 0; i < count; i++)
                data.add(Math.random());
            Double avg = data.stream()
                            .mapToDouble(Double::doubleValue)
                            .summaryStatistics()
                            .getAverage();
            return avg;
        }
    }
}
