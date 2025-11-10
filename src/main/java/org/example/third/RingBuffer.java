package org.example.third;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RingBuffer<T> {

    private final Object[] buffer;
    private final Integer capacity;
    private int head = 0;
    private int tail = 0;

    private final AtomicInteger size = new AtomicInteger(0);
    private final Lock writeLock = new ReentrantLock();
    private final Lock readLock = new ReentrantLock();


    public RingBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }

    public boolean offer(T value) {

        int indexToWrite; // для оптимизации при записи
        writeLock.lock();
        try {
            if (size.get() == capacity) return false;
            indexToWrite = head;
            head = (head + 1) % capacity;
            size.incrementAndGet();
        } finally {
            writeLock.unlock();
        }

        buffer[indexToWrite] = value;
        return true;
    }

    @SuppressWarnings("unchecked")
    public T poll() {
        readLock.lock();
        try {
            if (size.get() == 0) {
                return null;
            }
            T value = (T) buffer[tail];
            tail = (tail + 1) % capacity;
            size.decrementAndGet();
            return value;
        } finally {
            readLock.unlock();
        }
    }

    public boolean isEmpty() {
        return size.get() == 0;
    }

    public boolean isFull() {
        return size.get() == capacity;
    }
}

