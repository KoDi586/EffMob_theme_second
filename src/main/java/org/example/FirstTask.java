package org.example;

public class FirstTask {

    public void execute(Integer integer) {

        ForSynchronized fs = new ForSynchronized();

        Runnable first_runnable = () -> {

            System.out.println("First thread is started");

            for (int i = 0; i <= integer; i+=2) {

                synchronized (fs) {
                    fs.print(i);
                    try {
                        fs.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted exception is in first_runnable");
                    }
                }
            }
            Thread.currentThread().interrupt();

        };

        Runnable second_runnable = () -> {
            System.out.println("Second thread is started");

            for (int i = 1; i <= integer; i+=2) {

                synchronized (fs) {
                    fs.print(i);
                    try {
                        fs.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted exception in second_runnable");
                    }
                }
            }
            Thread.currentThread().interrupt();

        };

        Thread thread1 = new Thread(first_runnable);
        Thread thread2 = new Thread(second_runnable);

        thread1.start();
        thread2.start();

    }

}

class ForSynchronized {

    public synchronized void print(Integer integer) {

        try {
            System.out.println(integer);
            notifyAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}