package queue;

public interface Queue {

    // Model: a[1..n]
    // immutable(l..r): l >= 1 && r <= n && a[i] == a'[i] for i = l..r && sequence is maintained on l..r
    // Inv: a[i] != null for i = 1..n && n >= 0

    // Pre: element != null
    // Post: n' == n + 1 && a[n'] == element && immutable(1..(n'-1))
    public void enqueue(Object element);

    // Pre: size > 0
    // Post: R == a[1] && immutable(1..n)
    public Object element();

    // Pre: size > 0
    // Post: R == a[0] && n' == n - 1 && sequence is maintained on 2..n
    public Object dequeue();

    // Pre: true
    // Post: R = size == 0 && immutable(1..n)
    public boolean isEmpty();

    // Pre: true
    // Post: R = size && immutable(1..n)
    public int size();

    // Pre: true
    // Post: n == 0 (a[1..n] is empty) && size == 0
    public void clear();

    // Pre: true
    // Post: leave only unique elements
    // first(element) == {min 1..k: a[k] == element}
    // for all elements:
    // 1) a[k] == element && first(element) < k => remove a[k] (other sequence is maintained)
    // 2) first(element1) < first(element2) => first'(element1) < first'(element2)
    // :NOTE: не описывайте контракты блоками кодами, опишите формально
    public void distinct();
}
