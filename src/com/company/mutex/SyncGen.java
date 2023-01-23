package com.company.mutex;

import java.util.Random;
/*
Первый поток генерирует случайные двузначные числа.
Как только появляется число, которое делится на 7, он передает управление, а также число второму потоку.
Второй поток выводит число в консоль, после чего передает управление первому потоку.
 */

public class SyncGen {
    static Object mutex = new Object();
    static ConsoleWriter consoleWriter = new ConsoleWriter();

    public static void main(String[] args) {
        new Thread(new RandGenerator()).start();
        new Thread(consoleWriter).start();
    }

    static class RandGenerator implements Runnable{
        Random random = new Random();
        @Override
        public void run() {
            while(true) {
                int rand = random.nextInt(90) + 10;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (mutex) {
                    if (rand % 7 == 0) {
                        consoleWriter.setNumber(rand);
                        mutex.notify();
                        try {
                            mutex.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    static class ConsoleWriter implements Runnable{
        private int number = 0;
        @Override
        public void run() {
            while(true) {
                synchronized (mutex) {
                    if (number != 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(number);
                        number = 0;
                        mutex.notify();
                        try {
                            mutex.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}