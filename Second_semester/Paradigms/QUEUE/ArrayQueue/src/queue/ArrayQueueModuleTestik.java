package queue;

import static queue.ArrayQueueModule.printQueue;

class ArrayQueueModuleTestik {
    public static void main(String[] args) {
        System.out.println("1-st");
        for (int i = 0; i < 10; i++) {
            ArrayQueueModule.enqueue(i);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(ArrayQueueModule.dequeue());
        }
        for (int i = 10; i < 40; i++) {
            ArrayQueueModule.enqueue(i);
            printQueue("");
        }

        System.out.println("2-nd");
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.enqueue(i);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(ArrayQueueModule.dequeue());
        }
        for (int i = 10; i > 5; i--) {
            ArrayQueueModule.enqueue(i);
            printQueue("");
        }
    }

}