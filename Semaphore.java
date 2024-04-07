import java.util.LinkedList;
import java.util.Random;

public class Semaphore {
    long N = 100;
    int mutex = 1;
    int empty = (int) N;
    int full = 0;
    LinkedList<Integer> linkedList = new LinkedList<>();
    Random rand = new Random();


    public static void main(String[] args) {
        Semaphore sc = new Semaphore();
        Thread producer = new Thread(sc::producer);
        Thread consumer = new Thread(sc::consumer);
        producer.start();
        consumer.start();
    }

    public void producer() {
        while (true) {
            int item = produce_item();
            down(empty);
            down(mutex);
            insertItem(item);
            up(mutex);
            up(full);
        }
    }

    public void consumer() {
    while (true) {
        down(full);
        down(mutex);
        if (!linkedList.isEmpty()) {
            int item = removeItem();
            up(mutex);
            up(empty);
            consumeItem(item);
        } else {
            up(mutex);
        }
    }
}


    public int produce_item() {
        return rand.nextInt(100);
    }

    public void down(int a) {
        a--;
    }

    public void up(int a) {
        a++;
    }

    public void insertItem(int a) {
        linkedList.add(a);
    }

    public void consumeItem(int a) {
        System.out.println("The square is::" + a * a);
    }

    public int removeItem() {
        return linkedList.remove();
    }

}
