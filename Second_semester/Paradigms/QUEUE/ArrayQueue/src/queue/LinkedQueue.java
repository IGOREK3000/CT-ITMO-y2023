package queue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Spliterator;

public class LinkedQueue extends AbstractQueue {
    private Node head;

    private Node tail;

    public LinkedQueue() {
        super();
        head = null;
        tail = null;
    }

    private class Node {
        private Object value;
        private Node next;
        public Node(Object value, Node next) {
            assert value != null;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void enqueueImpl(Object element) {
        Node node = new Node(element, head);
        if (size == 1) {
            tail = node;
            head = node;
        } else {

            head.next = node;
            head = node;
        }
    }

    @Override
    public Object elementImpl() {
        return tail.value;
    }

    @Override
    public Object dequeueImpl() {
        Node res = tail;
        if (isEmpty()) {
            tail = null;
        } else {
            tail = tail.next;
        }
        return res.value;
    }


    @Override
    public void clearImpl() {
        tail = null;
        head = null;
    }

}
