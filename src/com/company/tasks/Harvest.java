package com.company.tasks;

/*
Имеются 3 ряда для сборщиков урожая. Каждому сборщику выделен один ряд.
Пока все не достигнут конца рядов, трактор ожидает окончания их работы.
Затем взвешивает выгружает весь собранный урожай на склад.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Harvest {
    static int number = 3;
    static List<Integer> result = new ArrayList<>();
    static CyclicBarrier cyclicBarrier;

    public static void main(String[] args) {
        cyclicBarrier= new CyclicBarrier(number, new Traktor());
        for(int i=0; i<number; i++){
            Thread worker = new Thread(new Harvester());
            worker.setName("Thread "+ i );
            worker.start();
        }


    }


    static class Harvester implements Runnable {
        int count = 3; //Урожай с одного ряда, яблок
        int number = 0; //Собранное количество яблок

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            for (int i = 0; i < count; i++) {
                try {
                    Thread.sleep(((int) (Math.random() * 100)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + ": Pick apple! Final result - " + ++number);
            }
            result.add(number);
            try {
                System.out.println(name + " waiting for others.");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    static class Traktor implements Runnable {
        @Override
        public void run() {
            String name = "Traktor";
            System.out.println(name + ": Computing sum of " + number);
            int sum = 0;
            for (Integer count : result) {
                System.out.print(count+" ");
                sum += count;
            }
            System.out.println(name + ": Final result = " + sum);
        }
    }
}
