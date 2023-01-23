package com.company;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer1 {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);
        Object obj = new Object();
        Consumer consumer = new Consumer(queue);
        Producer producer = new Producer(queue);

        Thread tconsumer = new Thread(consumer, "consumer");
        Thread tproducer = new Thread(producer, "producer");
        //Старт
        tconsumer.start();
        tproducer.start();
    }
    static class Producer implements Runnable{
        BlockingQueue<String> queue;

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true)
                try {
                    synchronized (this) {
                        String name = Thread.currentThread().getName();
                        queue.put(name);
                        System.out.println(name + " " + queue.size());
                    }
                    Thread.sleep((long) (Math.random()*1000+500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
   static class Consumer implements Runnable {
        BlockingQueue<String> queue;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true)
                try {
                    synchronized (this) {
                        String consumer = Thread.currentThread().getName();
                        String name = queue.take();
                        System.out.println(consumer + " " + queue.size());
                    }
                    Thread.sleep((long) (Math.random()*1000+1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

}
