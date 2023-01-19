package com.company.runflag;

public class RunnersFlag {
    public static void main(String[] args) {
        Runner runner1 = new Runner("1");
        Thread thread = new Thread(runner1);

        thread.start();
        Runner runner2 = new Runner("2");
        Thread thread2= new Thread(runner2);
        thread2.start();
        try {

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //runner1.flag= true;
        //runner2.flag= true;

        thread.interrupt();
        thread2.interrupt();
    }
}
class Runner implements Runnable{
    String name;
    boolean flag =false;

    public Runner(String name) {

        this.name
                = name;
    }

    @Override
    public void run() {

int i=0;
       while(!Thread.currentThread().isInterrupted()){
           System.out.printf("Runner %s is running, step: %d%n",name,i++);
           try {
               Thread.sleep(100);
           } catch (InterruptedException e) {
               e.printStackTrace();
               return;
           }
       }

    }
}