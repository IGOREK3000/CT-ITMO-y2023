package queue;

import java.util.Arrays;
import java.util.function.Predicate;

public class ArrayQueueModule {
    private static Object[] arrayQueue = new Object[2];

    private static int size = 0;

    private static int head = -1;

    // Model: a[1..n]
    // immutable(l..r): l >= 1 && r <= n && a[i] == a'[i] for i = l..r && sequence is maintained on l..r
    // Inv: a[i] != null for i = 1..n && n >= 0

    // Pre: element != null
    // Post: n' == n + 1 && a[n'] == element && immutable(1..(n'-1))
    public static void enqueue(Object element) {
        head = pos(head + 1);
        size++;
        ensureSize();
        arrayQueue[head] = element;
    }

    // Pre: capacity >= 0
    // Post: immutable(1..n) && (Detail of realization: extends capacity of inner array by 2)
    private static void ensureSize() {
        int capacity = arrayQueue.length;
        if (size == capacity) {
            Object[] newArrayQueue = new Object[capacity * 2];
            System.arraycopy(Arrays.copyOfRange(arrayQueue, 0, head), 0,
                    newArrayQueue, 0, head);
            System.arraycopy(Arrays.copyOfRange(arrayQueue, head, capacity), 0,
                    newArrayQueue, head + capacity, capacity - head);
            arrayQueue = newArrayQueue;
        }
    }

    // Pre: True
    // Post: R == n % capacity (0 <= n < capacity) && immutable(1..n)   (Details of realization of private method)
    private static int pos(int n) {
        int capacity = arrayQueue.length;
        return n >= 0 ? n % capacity : capacity + n % capacity;
    }


    // Pre: size > 0
    // Post: R == a[1] && immutable(1..n) && n == n'
    public static Object element() {
        assert size != 0;
        return arrayQueue[getTail()];
    }

    // Pre: True
    // Post: R == head - size + 1 == tail && immutable(1..n)          (Details of realization of private method)
    private static int getTail() {
        return pos(head - size + 1);
    }


    // Pre: size > 0
    // Post: R == a[0] && n' == n - 1 && sequence is maintained on 2..n
    public static Object dequeue() {
        int tail = getTail();
        Object result = arrayQueue[tail];
        arrayQueue[tail] = null;
        size--;
        return result;
    }

    // Pre: True
    // Post: n == n' && immutable(1..n) && Displays queue and params on System.out && immutable(1..n)
    public static void printQueue(String message) {
        System.out.println(message);
        for (Object obj : arrayQueue) {
            System.out.print(obj + " ");
        }
        System.out.println("\n");
    }

    // Pre: true
    // Post: n == n' && R = size == 0 && immutable(1..n)
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: R = size && immutable(1..n) && n == n'
    public static int size() {
        return size;
    }

    // Pre: true
    // Post: n == 0 (a[1..n] is empty) && size == 0
    public static void clear() {
        while (!isEmpty()) {
            dequeue();
        }
    }

    // Pre: true && predicate != null
    // Post: n == n' && immutable(1..n) && R = min(k : predicate.test(a[k]) == true)
    public static int indexIf(Predicate<Object> predicate) {
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
    //Post: n == n' && immutable(1..n) && R = max(k : predicate.test(a[k]) == true)
    public static int lastIndexIf(Predicate<Object> predicate) {
        assert predicate != null;
        for (int i = size; i > 0; i--) {
            if (predicate.test(arrayQueue[pos(head - size + i)])) {
                return i - 1;
            }
        }
        return -1;
    }
}

