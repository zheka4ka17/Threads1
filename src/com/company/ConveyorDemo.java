package com.company;
/*
На конвейере 3 рабочих, каждый из которых выполняет операцию движущимися по конвейеру изделиями.
Все работают с разной скоростью, но при этом последовательность операций 1-2-3 не должна быть нарушена.
 */

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConveyorDemo {
    static AtomicInteger indWorker = new AtomicInteger(1);
    static AtomicInteger indProduct = new AtomicInteger(1);
    static List<Product> products = Arrays.asList(new Product[]{new Product(), new Product(), new Product()});
    static Conveyor conveyor = new Conveyor();

    public static void main(String[] args) {
        new Thread(new Worker()).start();
        new Thread(new Worker()).start();
        new Thread(new Worker()).start();

    }

    static class Product{
        public int id = indProduct.getAndIncrement();
        public int stage = 0;
    }

    static class Worker implements Runnable{
        public int id = indWorker.getAndIncrement();

        @Override
        public void run() {
            for (int i=0; i<products.size(); i++)
                conveyor.process(this,products.get(i));



        }
    }

    static class Conveyor{
        public synchronized void process(Worker worker, Product product){
            while (product.stage!=worker.id-1){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Worker: %d, product: %d%n", worker.id, product.id);
            product.stage++;
            notify();
        }
    }
}