package queue;

import java.util.Arrays;
import java.util.function.Predicate;

public class ArrayQueue {
    private static Object[] arrayQueue;

    private static int capacity;

    private static int size;

    private static int head;

    private static int tail;

    /*
     ......

     */

    // Model: a[1..n]
    //

    public ArrayQueue() {
        arrayQueue = new Object[2];
        capacity = arrayQueue.length;
        size = 0;
        head = -1;
        tail = 0;
    }
    // Pred: element != null
    // Post: n' == (n + 1) % size && a[n'] ==
    public void enqueue(Object element) {
        head = pos(head + 1);
        ensureSize(head);
        size++;
        arrayQueue[head] = element;
    }

    // Pred:
    // Post:
    private void ensureSize(int top) {
        int h = pos(top);
        int t = pos(tail);
        if (h == t && size != 0)  {
            Object[] newArrayQueue = new Object[capacity * 2];
            System.arraycopy(Arrays.copyOfRange(arrayQueue, 0, h), 0, newArrayQueue, 0, h);
            System.arraycopy(Arrays.copyOfRange(arrayQueue, h, capacity), 0, newArrayQueue, h + capacity, capacity - h);
            capacity *= 2;
            head = pos(head);
            tail = pos((tail + capacity / 2));
            arrayQueue = newArrayQueue;
        }
    }

    private int pos(int n) {
        return n % capacity;
    }


    // Pred:
    // Post:
    public Object element() {
        return arrayQueue[tail];
    }

    public void printParams() {
        System.out.println("Size: " + size + " Tail: " + tail + " Head: " + head);
    }

    // Pred: size > 0
    // Post:
    public Object dequeue() {
        Object result = arrayQueue[tail];
        arrayQueue[tail] = null;
        size--;
        tail = pos(tail + 1);
        return result;
    }

    // Pred:true
    // Post: R = size == 0
    public boolean isEmpty() {
        return size == 0;
    }


    // Pred: true
    // Post: R = size
    public int size() {
        return size;
    }

    public void clear() {
        arrayQueue = new Object[2];
        capacity = arrayQueue.length;
        size = 0;
        head = -1;
        tail = 0;
    }

    //Pre: true
    //Post: R = min(k : predicate.test(a[k]) == true)
    public int indexIf(Predicate<Object> predicate) {
        if (head >= tail) {
            for (int i = tail; i <= head; i++) {
                if (predicate.test(arrayQueue[i])) {
                    return (i - tail);
                }
            }
        } else {
            for (int i = tail; i < arrayQueue.length; i++) {
                if (predicate.test(arrayQueue[i])) {
                    return i - tail;
                }
            }
            for (int i = 0; i <= head; i++) {
                if (predicate.test(arrayQueue[i])) {
                    return capacity - tail + i;
                }
            }

        }
        return -1;
    }

    //Pre: true
    //Post: R = max(k : predicate.test(a[k]) == true)
    public int lastIndexIf(Predicate<Object> predicate) {
        int ind = -1;
        if (head >= tail) {
            for (int i = tail; i <= head; i++) {
                if (predicate.test(arrayQueue[i])) {
                    ind = i - tail;
                }
            }
        } else {
            for (int i = tail; i < arrayQueue.length; i++) {
                if (predicate.test(arrayQueue[i])) {
                    ind = i - tail;
                }
            }
            for (int i = 0; i <= head; i++) {
                if (predicate.test(arrayQueue[i])) {
                    ind = capacity - tail + i;
                }
            }

        }
        return ind;
    }
}
