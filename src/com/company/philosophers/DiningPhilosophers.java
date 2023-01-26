package com.company.philosophers;

import java.util.concurrent.locks.ReentrantLock;

/*
Пять безмолвных философов сидят вокруг круглого стола, перед каждым философом стоит тарелка спагетти.
Вилки лежат на столе между каждой парой ближайших философов.
Каждый философ может либо есть, либо размышлять. Приём пищи не ограничен количеством
оставшихся спагетти — подразумевается бесконечный запас. Тем не менее, философ может есть
только тогда, когда держит две вилки — взятую справа и слева (альтернативная формулировка
проблемы подразумевает миски с рисом и палочки для еды вместо тарелок со спагетти и вилок).
Каждый философ может взять ближайшую вилку (если она доступна) или положить —
если он уже держит её. Взятие каждой вилки и возвращение её на стол являются
раздельными действиями, которые должны выполняться одно за другим.
Вопрос задачи заключается в том, чтобы разработать модель поведения (параллельный алгоритм),
при котором ни один из философов не будет голодать, то есть будет вечно чередовать приём пищи
и размышления.
Взаимная блокировка (deadlock) - надо запустить несколько раз.
 */
public class DiningPhilosophers {
    private static ReentrantLock mutex = new ReentrantLock();


    public static void main(String[] args) {
        int count =  3;
        Philosopher [] philosophers = new Philosopher[count];
        Object [] forks = new Object[count];

        for(int i=0 ; i<count; i++)
            forks[i]= new Object();

        for (int i=0; i<count; i++) {
            philosophers[i] = new Philosopher(mutex,forks[i], forks[(i + 1) % count]);
            new Thread(philosophers[i], "Philosopher"+(i+1)).start();
        }

    }
}
class Philosopher implements Runnable{
    ReentrantLock mutex;
    private Object leftFork;
    private Object rightFork;

    public Philosopher(ReentrantLock mutex,Object leftFork, Object rightFork) {
        this.mutex=mutex;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // thinking
                doAction(": Thinking");
                mutex.lock();
                synchronized (leftFork) {
                    doAction(": Picked up left fork");
                    synchronized (rightFork) {
                        // eating
                        doAction(": Picked up right fork - eating");
                        mutex.unlock();
                        doAction(": Put down right fork");
                    }
                    // Back to thinking
                    doAction(": Put down left fork. Back to thinking");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
    private void doAction(String action) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " " + action);
        Thread.sleep(((int) (Math.random() * 100)));
    }
}
