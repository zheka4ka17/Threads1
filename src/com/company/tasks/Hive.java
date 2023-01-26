package com.company.tasks;
/*
В одном лесу живут n пчел и один медведь, которые используют один горшок меда, вместимостью N глотков.
Сначала горшок пустой. Пока горшок не наполнится, медведь спит. Как только горшок заполняется,
медведь просыпается и съедает весь мед, после чего снова засыпает. Каждая пчела многократно собирает
по одному глотку меда и кладет его в горшок. Пчела, которая приносит последнюю порцию меда, будит медведя.
Создать многопоточное приложение, моделирующее поведение пчел и медведя.
 */


import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Hive {  //улий
    static BlockingQueue<Integer> pot = new LinkedBlockingQueue<>(5);
    static int count = 3;
    static Object object= new Object();

    public static void main(String[] args) {
        new Thread(new Winnipeg()).start();
        for (int i=0; i<count; i++){
            new Thread(new Bee()).start();
        }

    }
    static class Bee implements Runnable {
        @Override
        public void run() {
            while (true)
                try {
                    synchronized (object) {
                        String name = Thread.currentThread().getName();
                        pot.put(1);
                        System.out.println(name + " " + pot.size());
                    }
                    Thread.sleep((long) (Math.random() * 1000 + 500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    static class Winnipeg implements Runnable {
        @Override
        public void run() {
            String name = "Winnipeg";
            while (true)
                try {
                    if (pot.size() == 5)
                        System.out.printf("%s eats %d liters of honey: %n", name, 5);
                    pot.clear();

                    Thread.sleep((long) (Math.random() * 1000 + 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
}
