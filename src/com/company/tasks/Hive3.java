package com.company.tasks;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/*
Задача о Винни-Пухе - 3 или неправильные пчелы - 2. N пчел
живет в улье, каждая пчела может собирать мед и сторожить улей (N>3). Ни
одна пчела не покинет улей, если кроме нее в нем нет других пчел. Каждая
пчела приносит за раз одну порцию меда. Всего в улей может войти десять
порций меда. Вини-Пух спит пока меда в улье меньше половины, но как
только его становится достаточно, он просыпается и пытается достать весь
мед из улья. Если в улье находится менее чем три пчелы, Вини-Пух забирает
мед, убегает, съедает мед и снова засыпает. Если в улье пчел больше, они
кусают Вини-Пуха, он убегает, лечит укус, и снова бежит за медом. Создать
многопоточное приложение, моделирующее поведение пчел и медведя.
 */
public class Hive3 {
    static BlockingQueue<Integer> hive = new LinkedBlockingQueue<>(10);
    static final AtomicInteger idBee = new AtomicInteger(); //Нумерация каждой пчелы
    //Пчелы-сторожа
    static ArrayBlockingQueue<Bee> guards = new ArrayBlockingQueue<>(5);

    static Object object = new Object();

    public static void main(String[] args) {
        new Thread(new Winnipeg()).start();
        for (int i = 0; i < 7; i++) {
            new Thread(new Bee()).start();

        }
    }

    static class Bee implements Runnable{
        int id = idBee.getAndIncrement();

        public int getId() {
            return id;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.printf("The bee %d peeks a honey%n", id);
                    waiting(1000);
                    synchronized (object) {
                        hive.offer(1);
                    }
                    //Если очередь защитников не переполнена, то пчела может вступить в их ряды
                    if (guards.offer(this)) {
                        System.out.printf("The bee %d defends the hive, count of guards: %d, honey: %d%n", id, guards.size(), hive.size());
                        waiting(700);
                        guards.take(); //Летит за медом
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupt...");
            }
        }
    }

    static class Winnipeg implements Runnable {
        @Override
        public void run() {
            String name = "Winnipeg";
            while (true) {
                if (hive.size() >= 5) {
                    if (guards.size() >= 3) {
                        System.out.printf("%s is bitten and runs away, counts of guards: %d%n", name, guards.size());
                        try {
                            guards.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        waiting(2000);
                    } else {
                        synchronized (object) {
                            int honey = hive.size();
                            hive.clear();
                            System.out.printf("%s ate all of honey: %d, counts of guards: %d%n", name, honey, guards.size());
                        }
                        waiting(1000);
                    }
                }
            }
        }
    }

    public static void waiting(int maxDelay) {
        try {
            Thread.sleep(((int) (Math.random() * maxDelay)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
