package com.company.basic;

import java.util.SortedMap;

public class ThreadDemo extends Thread {
    public static void main(String[] args) {
        System.out.println("Start : "+ Thread.currentThread().getName());
        ThreadDemo threadDemo = new ThreadDemo();
        threadDemo.start();
        try {
            threadDemo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finish : "+ Thread.currentThread().getName());
    }

    @Override
    public void run() {
        System.out.println("Start :"+ Thread.currentThread().getName());
        System.out.println("Finish : "+ Thread.currentThread().getName());

    }
}
