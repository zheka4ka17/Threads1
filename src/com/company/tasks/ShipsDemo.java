/*
Транспортные корабли входят в бухту и далее плывут к причалам для погрузки разного рода товара.
В бухте одновременно могут находиться только 5 кораблей. Корабли генерируются случайным образом.
Вид кораблей и их вместительность могут быть разными в зависимости от типа товаров, которые нужно загрузить на корабль.
Например, есть три типа кораблей (Хлеб, Банан и Одежда) и три вида вместительности 10, 50, 100 шт. товаров.
Далее есть 3 вида причалов для погрузки кораблей — Хлеб, Банан и Одежда. Каждый причал берет или подзывает к себе
необходимый ему корабль и начинает его загружать. За одну секунду причал загружает на корабль 10 ед. товара.
То есть если у корабля вместительность 50 шт., то причал загрузит его за 5 секунд своей работы.
Написать многопоточное приложение.
 */ package com.company.tasks;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ShipsDemo {
    static AtomicInteger idShip = new AtomicInteger();
    static ArrayBlockingQueue<Ship> bay = new ArrayBlockingQueue<>(5);
    static Semaphore[] docks = new Semaphore[3];
    static {
        for(int i=0; i<3; i++)
            docks[i]= new Semaphore(1);
    }


    public static void main(String[] args) {
        new Thread(new ShipGenerator()).start();
    }
    enum Type {Bread, Banana, Cloth}

    static class Ship implements Runnable {
        int id = idShip.getAndIncrement();
        Type type; //Тип товара
        int capacity; //Вместимость корабля
        int volume = 0; //Текущая загрузка

        public Ship(Type type, int capacity) {
            this.type = type;
            this.capacity = capacity;
        }

        @Override
        public void run() {
            System.out.printf("The ship %d is trying to get into the bay%n", id);
            while (!bay.offer(this))
                waiting(1000);
            System.out.printf("The ship %d is in the bay (free: %d)%n", id, bay.remainingCapacity());
            try {
                int index = type.ordinal();
                docks[index].acquire();
                System.out.printf("The ship %d is loading with %s%n", id, type);
                while (volume < capacity) {
                    volume += 10;
                    waiting(1000);
                }
                docks[index].release();
                System.out.printf("The ship %d is sailing%n", id);
                bay.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public int getId() {
            return id;
        }
    }

    static class ShipGenerator implements Runnable {

        @Override
        public void run() {
            while (true){
                int indLoad = (int) (Math.random()*3); //Тип груза
                int indCapacity = (int) (Math.random()*3);
                int capacity = 0;
                switch (indCapacity){
                    case 0: capacity = 10; break;
                    case 1: capacity = 50; break;
                    case 2: capacity = 100; break;
                }

//System.out.println(Type.values()[indLoad]);
                new Thread(new Ship(Type.values()[indLoad], capacity)).start();
                waiting(1000);
            }
        }
    }

    public static void waiting(int maxDelay) {
        try {
            Thread.sleep(((int) (Math.random() * (maxDelay-300)+300)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
