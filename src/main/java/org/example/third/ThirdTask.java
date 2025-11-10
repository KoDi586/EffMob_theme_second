package org.example.third;

public class ThirdTask {

    public void execute() {

        RingBuffer<Integer> ringBuffer = new RingBuffer<>(5);

        Thread writer = new Thread(() -> {
            int value = 1;
            while (true) {
                boolean success = ringBuffer.offer(value);
                if (success) {
                    System.out.println("[WRITE] Добавлено: " + value);
                    value++;
                } else {
                    System.out.println("[WRITE] Буфер полон, ожидаю...");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    Thread.sleep(100); // поток пишет быстрее, чем читает
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread reader = new Thread(() -> {
            while (true) {
                Integer value = ringBuffer.poll();
                if (value != null) {
                    System.out.println("          [READ] Прочитано: " + value);
                } else {
                    System.out.println("          [READ] Буфер пуст, ожидаю...");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    Thread.sleep(300); // читает медленнее, чем пишет
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        writer.start();
        reader.start();
    }

}
