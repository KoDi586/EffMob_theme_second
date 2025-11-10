package org.example.third;

public class RingBuffer<T> {
    private final Object[] buffer;
    private final int capacity;
    private int head = 0; // куда писать
    private int tail = 0; // откуда читать
    private int size = 0; // сколько элементов в буфере

    public RingBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }

    public boolean offer(T value) { // добавить элемент
        if (size == capacity) {
            return false; // буфер полон
        }
        buffer[head] = value;
        head = (head + 1) % capacity;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    public T poll() { // забрать элемент
        if (size == 0) {
            return null; // буфер пуст
        }
        T value = (T) buffer[tail];
        tail = (tail + 1) % capacity;
        size--;
        return value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }
}

