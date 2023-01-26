package com.company.tasks;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExchangerDemo {
    static Exchanger<String> exchanger;

    public static void main(String[] args) {
exchanger = new Exchanger<>();
new Thread(new User()).start();
new Thread(new User()).start();


    }

    static class User implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(((int) (Math.random() * 100)));
                String name = Thread.currentThread().getName();
                String str =
                        exchanger.exchange
                                (name, 50, TimeUnit.MILLISECONDS);
                System.out.println(name+" got message "+str);
            } catch (InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
