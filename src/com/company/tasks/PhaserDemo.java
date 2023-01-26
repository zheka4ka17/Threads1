package com.company.tasks;

import java.util.concurrent.Phaser;

public class PhaserDemo {
    static Phaser phaser;

    public static void main(String[] args) {
    phaser = new Phaser(3);
    new Thread(new Participant()).start();
    new Thread(new Participant()).start();
    new Thread(new Participant()).start();

        int  phase;

        phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");

        phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");


        phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");

    }

    static class Participant implements Runnable {
        public Participant() {
          //  phaser.register();
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            try {
                System.out.println(name + " выполняет фазу " + phaser.getPhase());
                Thread.sleep(((int) (Math.random() * 100)));
                phaser.arriveAndAwaitAdvance(); // сообщаем, что первая фаза достигнута

                System.out.println(name + " выполняет фазу " + phaser.getPhase());
                Thread.sleep(((int) (Math.random() * 100)));
                phaser.arriveAndAwaitAdvance(); // сообщаем, что вторая фаза достигнута

                System.out.println(name + " выполняет фазу " + phaser.getPhase());
                Thread.sleep(((int) (Math.random() * 100)));
                phaser.arriveAndDeregister(); // сообщаем о завершении фаз и удаляем с регистрации

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
