package com.company.tasks;
/*
Спящий брадобрей.
Рассмотрим парикмахерскую, в которой работает один парикмахер, имеется одно кресло для стрижки
и несколько кресел в приемной для посетителей, ожидающих своей очереди. Если в парикмахерской нет посетителей,
парикмахер засыпает прямо на своем рабочем месте. Появившийся посетитель должен его разбудить,
в результате чего парикмахер приступает к работе. Если в процессе стрижки появляются новые посетители,
они должны либо подождать своей очереди, либо покинуть парикмахерскую, если в приемной
нет свободного кресла для ожидания (т.е. это стратегия обслуживания с отказами).
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


   public class SleepingBarber {
    //В очереди два клиента и один у парикмахера
    static ArrayBlockingQueue<Customer> queue = new ArrayBlockingQueue<>(2);
    static final AtomicInteger idCustomer = new AtomicInteger(); //Нумерация каждого потока посетителя

    public static void main(String[] args) throws IOException {

        Thread barber = new Thread(new Barber());
        barber.start();
        waiting(1000);
        for (int i = 0; i < 9; ++i) {
            waiting(1000);
            System.out.println(String.format("The client %1$s came.", String.valueOf(i)));
            if (!queue.offer(new Customer())) {
                System.out.println(String.format("The client %1$s leaves, there are no sits.", String.valueOf(i)));
            }
        }


        System.in.read
                ();
        barber.interrupt();
    }

    static class Barber implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Customer client = queue.peek();
                    if (client == null)
                        System.out.println("Barber is sleeping.");
                    client = queue.take();
                    System.out.println(String.format("The client %1$d begins to cut the hair.", client.getId()));
                    waiting(3000);
                    System.out.println(String.format("The client %1$d got a haircut.", client.getId()));
                }
            } catch (InterruptedException e) {
                System.out.println("Go home...");
            }
        }
    }

    static class Customer {
        int id = idCustomer.getAndIncrement();

        public int getId() {
            return id;
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