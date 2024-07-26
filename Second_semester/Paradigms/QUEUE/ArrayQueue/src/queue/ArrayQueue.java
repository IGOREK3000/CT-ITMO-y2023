package queue;

import java.util.Arrays;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue {
    private Object[] arrayQueue;

    private int head;


    // Model: a[1..n]

    public ArrayQueue() {
        super();
        arrayQueue = new Object[10];
        head = -1;
    }

    public void enqueueImpl(Object element) {
        head = pos(head + 1);
        ensureSize();
        arrayQueue[head] = element;
    }

    public Object elementImpl() {
        return arrayQueue[getTail()];
    }

    public Object dequeueImpl() {
        int tail = pos(getTail() - 1);
        Object result = arrayQueue[tail];
        arrayQueue[tail] = null;

        return result;
    }

    public void clearImpl() {
        while (!isEmpty()) {
            dequeue();
        }
    }


    // Pre: true && predicate != null
    // Post: R = min(k : predicate.test(a[k]) == true) || R = -1
    public int indexIf(Predicate<Object> predicate) {
        assert predicate != null;
        int ind = -1;
        for (int i = size; i > 0; i--) {
            if (predicate.test(arrayQueue[pos(head - size + i)])) {
                ind = i - 1;
            }
        }
        return ind;
    }

    //Pre: true && predicate != null
    //Post: R = max(k : predicate.test(a[k]) == true) || R = -1
    public int lastIndexIf(Predicate<Object> predicate) {
        assert predicate != null;
        for (int i = size; i > 0; i--) {
            if (predicate.test(arrayQueue[pos(head - size + i)])) {
                return i - 1;
            }
        }
        return -1;
    }

    // Pre: capacity >= 0
    // Post: immutable(n) && capacity' = capacity*2
    private void ensureSize() {
        int capacity = arrayQueue.length;
        if (size == capacity)  {
            Object[] newArrayQueue = new Object[capacity * 2];
            System.arraycopy(Arrays.copyOfRange(arrayQueue, 0, head), 0,
                    newArrayQueue, 0, head);
            System.arraycopy(Arrays.copyOfRange(arrayQueue, head, capacity), 0,
                    newArrayQueue, head + capacity, capacity - head);
            arrayQueue = newArrayQueue;
        }
    }

    // Pre: True
    // Post: n = n % capacity (0 <= n < capacity)
    private int pos(int n) {
        int capacity = arrayQueue.length;
        return n >= 0 ? n % capacity : capacity + n % capacity;
    }

    // Pre: True
    // Post: R = head - size + 1 == tail
    private int getTail() {
        return pos(head - size + 1);
    }

    // Pre: True
    // Post: displays params and queue in System.out
    public void printQueue(String message) {
        System.out.println(message);
        System.out.println("Head: " + head + " Size: " + size);
        for (Object obj : arrayQueue) {
            System.out.print(obj + " ");
        }
        System.out.println("\n");
    }
}
