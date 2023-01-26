package com.company.philosophers;
/*
В кафе имеется столик на двоих, к которому выстроилась очередь из 5 человек.
Если к человеку дошла очередь, то за один раз он успевает съесть только одно блюдо,
после чего уступает место следующему.
Всего подается 3 блюда.
Написать многопоточное приложение для одновременного обслуживания клиентов.
 */

import java.util.concurrent.Semaphore;

public class TableForTwo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);
        for(int i = 1 ;i<6 ; i++)
            new Client(semaphore, i).start();

    }
}
class Client extends Thread{
    Semaphore semaphore; //Показывает количество свободных мест
    int count = 3; //Количество блюд
    int id; //ID клиента


    public Client(Semaphore semaphore, int id) {
        this.semaphore = semaphore;

        this.id = id;
    }

    public void run(){
        try{
            int index = 1;
            while (index<=count) {
                semaphore.acquire(); //Запросить место
                System.out.println("Клиент " + id + " садится за стол и ест блюдо "+index);
                sleep(200); //Ест
                index++;
                System.out.println("Клиент " + id + " выходит из-за стола");
                semaphore.release();
                sleep(200); //Гуляет
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
