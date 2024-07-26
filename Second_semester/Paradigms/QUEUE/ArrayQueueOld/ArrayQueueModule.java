package queue;

// :NOTE: unused imports
import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.function.Predicate;

// :NOTE: где тесты?
public class ArrayQueueModule {
    private static Object[] arrayQueue = new Object[2];

    private static int capacity = arrayQueue.length;

    // :NOTE: хранить size излишне
    private static int size = 0;

    private static int head = -1;

    private static int tail = 0;

    // Model: a[1..n], ind - index of element
    // :NOTE: arrayQueue -- детали реализации, ваша модель -- массив a[1..n]
    // immutable(n): arrayQueue[i] == arrayQueue'[i] for (i = tail; i != head; i = (i + 1) % capacity)
    //                && (arrayQueue[head] == arrayQueue'[head])
    // Inv: a[ind] != null && ind >= 0

    // Pre: element != null && arrayQueue.length != 0
    // :NOTE: пересчет head это также деталь реализации
    // Post: head' == (head + 1) % capacity &&
    //       a[head'] == element && immutable(n) ()
    public static void enqueue(Object element) {
        head = pos(head + 1);
        ensureSize(head);
        size++;
        arrayQueue[head] = element;
    }

    // Pre: capacity >= 0
    // Post: immutable(n) && capacity' = capacity*2
    private static void ensureSize(int top) {
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

    // Pre: Post
    // Post: n = n % capacity (0 <= n < capacity)
    private static int pos(int n) {
        return n % capacity;
    }


    // Pre: size != 0
    // Post: R = arrayQueue[tail] && immutable()
    public static Object element() {
        // :NOTE: assert?
        return arrayQueue[tail];
    }


    // Pre: size > 0
    // Post: (immutable(ind) / tail) && (result == a'[0]) && (ind == ind' - 1)
    public static Object dequeue() {
        Object result = arrayQueue[tail];
        arrayQueue[tail] = null;
        size--;
        tail = pos(tail + 1);
        return result;
    }

    // Pre:true
    // Post: R = size == 0 && immutable(ind)
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: R = size && immutable(n)
    public static int size() {
        return size;
    }

    // Pre: true
    // Post: ind = 0 && size == 0
    public static void clear() {
        arrayQueue = new Object[2];
        capacity = arrayQueue.length;
        size = 0;
        head = -1;
        tail = 0;
    }

    //Pre: true
    //Post: R = min(k : predicate.test(a[k]) == true)
    public static int indexIf(Predicate<Object> predicate) {
        // :NOTE: можно объединить оба случая
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
    public static int lastIndexIf(Predicate<Object> predicate) {
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

