package queue;

import java.util.Arrays;
import java.util.function.Predicate;

public class ArrayQueueADT {
    private Object[] arrayQueue = new Object[2];

    private int capacity = arrayQueue.length;

    private int size = 0;

    private int head = -1;

    private int tail = 0;



    public static ArrayQueueADT create() {
        ArrayQueueADT queue = new ArrayQueueADT();
        queue.capacity = queue.arrayQueue.length;
        queue.arrayQueue = new Object[2];
        queue.head = -1;
        return queue;
    }

    /*
     ......

     */

    // Model: a[1..n]
    //

    // Pred: element != null
    // Post: n' == (n + 1) % size && a[n'] ==
    public static void enqueue(ArrayQueueADT queue, Object element) {
        queue.head = pos(queue, queue.head + 1);
        ensureSize(queue, queue.head);
        queue.size++;
        queue.arrayQueue[queue.head] = element;
    }

    // Pred:
    // Post:
    private static void ensureSize(ArrayQueueADT queue, int top) {
        int h = pos(queue, top);
        int t = pos(queue, queue.tail);
        if (h == t && queue.size != 0)  {
            Object[] newArrayQueue = new Object[queue.capacity * 2];
            System.arraycopy(Arrays.copyOfRange(queue.arrayQueue, 0, h), 0, newArrayQueue, 0, h);
            System.arraycopy(Arrays.copyOfRange(queue.arrayQueue, h, queue.capacity), 0, newArrayQueue, h + queue.capacity, queue.capacity - h);
            queue.capacity *= 2;
            queue.head = pos(queue, queue.head);
            queue.tail = pos(queue, (queue.tail + queue.capacity / 2));
            queue.arrayQueue = newArrayQueue;

        }
    }

    private static int pos(ArrayQueueADT queue, int n) {
        return n % queue.capacity;
    }


    // Pred:
    // Post:
    public static Object element(ArrayQueueADT queue) {
        return queue.arrayQueue[queue.tail];
    }

    public static void printParams(ArrayQueueADT queue) {
        System.out.println("Size: " + size(queue) + " Tail: " + queue.tail + " Head: " + queue.head);
    }

    // Pred: size > 0
    // Post:
    public static Object dequeue(ArrayQueueADT queue) {
        Object result = queue.arrayQueue[queue.tail];
        queue.arrayQueue[queue.tail] = null;
        queue.size--;
        queue.tail = pos(queue, queue.tail + 1);
        return result;
    }

    // Pred:true
    // Post: R = size == 0
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }


    // Pred: true
    // Post: R = size
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    public static void clear(ArrayQueueADT queue) {
        queue.arrayQueue = new Object[2];
        queue.capacity = queue.arrayQueue.length;
        queue.size = 0;
        queue.head = -1;
        queue.tail = 0;
    }

    public static void push(ArrayQueueADT queue, Object element) {
        queue.tail = pos(queue, queue.tail - 1);
        ensureSize(queue, queue.head);
        queue.size++;
        queue.arrayQueue[queue.tail] = element;
    }

    public static Object peek(ArrayQueueADT queue) {
        return queue.arrayQueue[queue.head];
    }

    public static Object remove(ArrayQueueADT queue) {
        Object result = queue.arrayQueue[queue.head];
        queue.arrayQueue[queue.head] = null;
        queue.size--;
        queue.head = pos(queue, queue.head - 1);
        return result;
    }

    //Pre: true
    //Post: R = min(k : predicate.test(a[k]) == true)
    public static int indexIf(ArrayQueueADT queue, Predicate<Object> predicate) {
        if (queue.head >= queue.tail) {
            for (int i = queue.tail; i <= queue.head; i++) {
                if (predicate.test(queue.arrayQueue[i])) {
                    return (i - queue.tail);
                }
            }
        } else {
            for (int i = queue.tail; i < queue.arrayQueue.length; i++) {
                if (predicate.test(queue.arrayQueue[i])) {
                    return i - queue.tail;
                }
            }
            for (int i = 0; i <= queue.head; i++) {
                if (predicate.test(queue.arrayQueue[i])) {
                    return queue.capacity - queue.tail + i;
                }
            }

        }
        return -1;
    }

    //Pre: true
    //Post: R = max(k : predicate.test(a[k]) == true)
    public static int lastIndexIf(ArrayQueueADT queue, Predicate<Object> predicate) {
        int ind = -1;
        if (queue.head >= queue.tail) {
            for (int i = queue.tail; i <= queue.head; i++) {
                if (predicate.test(queue.arrayQueue[i])) {
                    ind = i - queue.tail;
                }
            }
        } else {
            for (int i = queue.tail; i < queue.arrayQueue.length; i++) {
                if (predicate.test(queue.arrayQueue[i])) {
                    ind = i - queue.tail;
                }
            }
            for (int i = 0; i <= queue.head; i++) {
                if (predicate.test(queue.arrayQueue[i])) {
                    ind = queue.capacity - queue.tail + i;
                }
            }

        }
        return ind;
    }
}
