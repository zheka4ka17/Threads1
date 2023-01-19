package com.company.basic;

public class ThreadSleep {
    public static void main(String[] args) {
        String[] messages = {
                "If you can keep your head when all about you",
                "Are losing theirs and blaming it on you,",
                "If you can trust yourself when all men doubt you,",
                "But make allowance for their doubting too;",
                "If you can wait and not be tired by waiting,",
                "Or being lied about, don’t deal in lies,",
                "Or being hated, don’t give way to hating,",
                "And yet don’t look too good, nor talk too wise."};

        Runnable runnable = () -> {
            for (int i = 0; i < messages.length; i++) {
                System.out.println(messages[i]);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ;
        };
        System.out.println("Creating Thread...");
        Thread thread = new Thread(runnable);


        System.out.println("Starting Thread...");
        thread.start();

    }
}
