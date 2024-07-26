package queue;

import static queue.ArrayQueueModule.printQueue;

class ArrayQueueTestik {
    public static void main(String[] args) {
        ArrayQueue arrayQueue1 = new ArrayQueue();
        ArrayQueue arrayQueue2 = new ArrayQueue();
        System.out.println("1-st");
        for (int i = 0; i < 10; i++) {
            arrayQueue1.enqueue(i);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(arrayQueue1.dequeue());
        }
        for (int i = 10; i < 40; i++) {
            arrayQueue1.enqueue(i);
            printQueue("");
        }

        System.out.println("2-nd");
        for (int i = 0; i < 5; i++) {
            arrayQueue2.enqueue(i);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(arrayQueue2.dequeue());
        }
        for (int i = 10; i > 5; i--) {
            arrayQueue2.enqueue(i);
            printQueue("");
        }
    }

}