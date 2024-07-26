package queue;

import java.util.Arrays;
import java.util.function.Predicate;

public class ArrayQueueADT {
    private Object[] arrayQueue = new Object[2];
    private int size = 0;
    private int head = -1;

    public static ArrayQueueADT create() {
        ArrayQueueADT queue = new ArrayQueueADT();
        queue.arrayQueue = new Object[2];
        queue.head = -1;
        return queue;
    }

    // Model: a[1..n]
    // immutable(l..r): l >= 1 && r <= n && a[i] == a'[i] for i = l..r && sequence is maintained on l..r
    // Inv: a[i] != null for i = 1..n && n >= 0

    // Pre: element != null
    // Post: n' == n + 1 && a[n'] == element && immutable(1..(n'-1))
    public static void enqueue(ArrayQueueADT queue, Object element) {
        queue.head = pos(queue, queue.head + 1);
        queue.size++;
        ensureSize(queue);
        queue.arrayQueue[queue.head] = element;
    }

    // Pre: capacity >= 0
    // Post: immutable(1..n) && (Detail of realization: extends capacity of inner array by 2)
    private static void ensureSize(ArrayQueueADT queue) {
        int capacity = queue.arrayQueue.length;
        if (queue.size == capacity)  {
            Object[] newArrayQueue = new Object[capacity * 2];
            System.arraycopy(Arrays.copyOfRange(queue.arrayQueue, 0, queue.head), 0,
                    newArrayQueue, 0, queue.head);
            System.arraycopy(Arrays.copyOfRange(queue.arrayQueue, queue.head, capacity), 0,
                    newArrayQueue, queue.head + capacity, capacity - queue.head);
            queue.arrayQueue = newArrayQueue;
        }
    }

    // Pre: True
    // Post: R == n % capacity (0 <= n < capacity) && immutable(1..n)   (Details of realization of private method)
    private static int pos(ArrayQueueADT queue, int n) {
        int capacity = queue.arrayQueue.length;
        return n >= 0 ? n % capacity : capacity + n % capacity;
    }


    // Pre: size > 0
    // Post: R == a[1] && immutable(1..n) && n == n'
    public static Object element(ArrayQueueADT queue) {
        assert queue.size != 0;
        return queue.arrayQueue[getTail(queue)];
    }

    // Pre: True
    // Post: R == head - size + 1 == tail && immutable(1..n)          (Details of realization of private method)
    private static int getTail(ArrayQueueADT queue) {
        return pos(queue, queue.head - queue.size + 1);
    }


    // Pre: size > 0
    // Post: R == a[0] && n' == n - 1 && sequence is maintained on 2..n
    public static Object dequeue(ArrayQueueADT queue) {
        int tail = getTail(queue);
        Object result = queue.arrayQueue[tail];
        queue.arrayQueue[tail] = null;
        queue.size--;
        return result;
    }

    // Pre: true
    // Post: R = size == 0 && immutable(1..n) && n == n'
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pre: true
    // Post: R = size && immutable(1..n) && n == n'
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pre: true
    // Post: n == 0 (a[1..n] is empty) && size == 0
    public static void clear(ArrayQueueADT queue) {
        while (!isEmpty(queue)) {
            dequeue(queue);
        }
    }

    // Pre: true && predicate != null
    // Post: n == n' && immutable(1..n) && R = min(k : predicate.test(a[k]) == true)
    public static int indexIf(ArrayQueueADT queue, Predicate<Object> predicate) {
        assert predicate != null;
        int ind = -1;
        for (int i = queue.size; i > 0; i--) {
            if (predicate.test(queue.arrayQueue[pos(queue,queue.head - queue.size + i)])) {
                ind = i - 1;
            }
        }
        return ind;
    }

    //Pre: true && predicate != null
    //Post: n == n' && immutable(1..n) && R = max(k : predicate.test(a[k]) == true)
    public static int lastIndexIf(ArrayQueueADT queue, Predicate<Object> predicate) {
        assert predicate != null;
        for (int i = queue.size; i > 0; i--) {
            if (predicate.test(queue.arrayQueue[pos(queue, queue.head - queue.size + i)])) {
                return i - 1;
            }
        }
        return -1;
    }
}
