package com.company.tasks;
/*
Имеется система параллельных процессов, которые взаимодействуют друг с другом следующим образом:
- все процессы делятся на два типа: процессы-читатели и процессы-писатели.
Процессы работают с общей областью памяти;
- процессы-читатели считывают, а процессы-писатели за писывают информацию в общую область памяти;
- одновременно может быть несколько активных процессов-читателей;
- при записи информации область памяти рассматривается как критический ресурс для всех процессов,
то есть, если работает процесс-писатель, то он должен быть единственным активным процессом.
Задача состоит в определении структуры управления, которая не приведет к тупику и не допустит нарушения критерия взаимного исключения.
 */

import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadersWriters {


    static volatile int countReaders = 0;
    static volatile int countWriters = 0;
    static Semaphore readLock = new Semaphore(3);
    static Semaphore writeLock = new Semaphore(1);
    static Object object = new Object(); //Некоторые действия нуждаются в синхронизации
    static final AtomicInteger idReader = new AtomicInteger(); //Маркировка каждого потока
    static final AtomicInteger idWriter = new AtomicInteger();

    public static void main(String[] args) {
        Random random = new Random();
        List<Thread> list = Stream.generate(()->random.nextInt(4)==0? new Thread(new Reader()) : new Thread(new Writer()))
                .limit(20)
                .collect(Collectors.toList());

        list.forEach(Thread::start);



    }

    static class Reader implements Runnable {
        int id = idReader.getAndIncrement();

        @Override
        public void run() {
            try {
                readLock.acquire();
                synchronized (object) {
                    if (countReaders == 0)
                        writeLock.acquire();
                }
                countReaders++;
                System.out.println("Reader " + id + " enters");
                System.out.println("Readers: " + countReaders + ", writers: " + countWriters);
                Thread.sleep(((int) (Math.random() * 100)));
                System.out.println("Reader " + id + " exits");
                countReaders--;

                synchronized (object) {
                    if (countReaders == 0)
                        writeLock.release();
                }
                readLock.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Writer implements Runnable {
        int id = idWriter.getAndIncrement();

        @Override
        public void run() {
            try {
                writeLock.acquire();
                countWriters++;
                System.out.println("Writer " + id + " enters");
                System.out.println("Writers: " + countWriters + " readers: " + countReaders);
                Thread.sleep(((int) (Math.random() * 100)));
                System.out.println("Writer " + id + " exits");
                countWriters--;
                writeLock.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
