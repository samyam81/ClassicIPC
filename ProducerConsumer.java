
import java.util.LinkedList;
import java.util.Random;

public class ProducerConsumer {
    // the code is for the PRODUCER-CONSUMER PROBLEM.
    long N = 100;
    int count = 0;
    LinkedList<Integer> linkedList = new LinkedList<>();

    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();
        Producer producer = pc.new Producer();
        Consumer consumer = pc.new Consumer();

        // Create producer and consumer threads
        Thread producerThread = new Thread(() -> producer.produce());
        Thread consumerThread = new Thread(() -> consumer.consume());

        // Start producer and consumer threads
        producerThread.start();
        consumerThread.start();
    }

    /**
     * InnerProducerConsumer
     */
    public class Producer {
        public void produce() {
            int item;
            while (true) {
                item = produce_item();
                if (count == N) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                insert(item);
                count++;
                if (count == 1) {
                    synchronized (linkedList) {
                        linkedList.notify(); // Wake up the consumer
                    }
                }
            }
        }

        public int produce_item() {
            Random rand = new Random();
            return rand.nextInt(1000); // Generates a random integer between 0 and 999
        }

        public void insert(int a) {
            linkedList.add(a);
        }
    }

    public class Consumer {
        public void consume() {
            int item;
            while (true) {
                synchronized (linkedList) {
                    if (count == 0) {
                        try {
                            linkedList.wait(); // Wait if the buffer is empty
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    item = remove();
                    count--;
                    if (count == N - 1) {
                        synchronized (linkedList) {
                            linkedList.notify(); // Wake up the producer
                        }
                    }
                }
                consume_item(item);
            }
        }

        public void consume_item(int item) {
            System.out.println("the square is::::::"+item * item);
        }

        public int remove() {
            return linkedList.remove(); // Removes and returns the first element of the linked list
        }
    }
}
