package queue;

import java.util.HashSet;

public abstract class AbstractQueue implements Queue {
    protected int size;

    protected AbstractQueue() {
        size = 0;
    }

    // Model: a[1..n]
    // immutable(l..r): l >= 1 && r <= n && a[i] == a'[i] for i = l..r && sequence is maintained on l..r
    // Inv: a[i] != null for i = 1..n && n >= 0


    // Pre: element != null
    // Post: n' == n + 1 && a[n'] == element && immutable(1..(n'-1))
    @Override
    public void enqueue(Object element) {
        assert element != null;
        size++;
        enqueueImpl(element);
    }

    // Pre: size > 0
    // Post: R == a[1] && immutable(1..n) && n == n'
    @Override
    public Object element() {
        assert size != 0;
        return elementImpl();
    }

    // Pre: size > 0
    // Post: R == a[0] && n' == n - 1 && sequence is maintained on 2..n
    @Override
    public Object dequeue() {
        assert size != 0;
        size--;
        return dequeueImpl();
    }

    // Pre: true
    // Post: R = size == 0 && immutable(1..n) && n == n'
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: R = size && immutable(1..n) && n == n'
    @Override
    public int size() {
        return size;
    }

    // Pre: true
    // Post: n == 0 (a[1..n] is empty) && size == 0
    @Override
    public void clear() {
        size = 0;
        clearImpl();
    }

    // Pre: true
    // Post:
    // first(element) == {min 1..k: a[k] == element}, default = -1
    // for (i = 1..n) if (first(element) == default) b[r] = element
    // a = b
    @Override
    public void distinct() {
        HashSet<Object> uniqueElements = new HashSet<>();
        int s = size;
        for (int i = 0; i < s; i++) {
            Object element = element();
            if (uniqueElements.contains(element)) {
                dequeue();
            } else {
                uniqueElements.add(element);
                dequeue();
                enqueue(element);
            }
        }
    }

    protected abstract void clearImpl();
    protected abstract Object dequeueImpl();
    protected abstract Object elementImpl();
    protected abstract void enqueueImpl(Object element);


}
