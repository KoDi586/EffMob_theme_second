package org.example;

public class FirstTask {

    public void execute(Integer integer) {

        ForSynchronized fs = new ForSynchronized();
        Object lock = new Object();

        Runnable first_runnable = () -> {

            System.out.println("First thread is started");

            for (int i = 0; i <= integer; i += 2) {
                synchronized (lock) {
                    fs.print(i);
                    lock.notify(); // Разбудить другой поток
                    if (i < integer) {
                        try {
                            lock.wait(); // Заснуть, чтобы дать вывести следующий
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        };

        Runnable second_runnable = () -> {
            System.out.println("Second thread is started");

            for (int i = 1; i <= integer; i += 2) {
                synchronized (lock) {
                    fs.print(i);
                    lock.notify();
                    if (i < integer) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        };

        Thread thread1 = new Thread(first_runnable, "First thread");
        Thread thread2 = new Thread(second_runnable, "Second thread");


        thread1.start();
        //чтобы точно выполнился второй поток после первого
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Both threads finished!");


    }

}

class ForSynchronized {

    public void print(Integer integer) {

        try {
            System.out.println(Thread.currentThread().getName() + " " + integer.toString());
//            notifyAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}