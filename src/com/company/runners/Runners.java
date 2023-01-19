package com.company.runners;

public class Runners {
    public static void main(String[] args) {
         Thread thread1=new Thread(new Runner1());
         thread1.setPriority(1);
         thread1.start();

        Thread thread2=new Thread(new Runner2());
        thread2.setPriority(10);
        thread2.start();

    }
}
class Runner1 implements Runnable{

    @Override
    public void run() {
        for (int i=0; i<5; i++) {
            System.out.println("Runner 1 is running, step: " + Integer.toString(i));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Runner2 implements Runnable{

    @Override
    public void run() {
        for (int i=0; i<5; i++) {
            System.out.println("Runner 2 is running, step: " + Integer.toString(i));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
