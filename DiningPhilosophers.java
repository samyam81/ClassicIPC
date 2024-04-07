
import java.util.Random;

public class DiningPhilosophers {
    static final int NUM_PHILOSOPHERS = 5;
    static boolean[] chopsticks = new boolean[NUM_PHILOSOPHERS];
    static Random rand = new Random();

    private static void philosopher(int id) {
        int left = id;
        int right = (id + 1) % NUM_PHILOSOPHERS;

        while (true) {
            think(id);
            pickUpChopsticks(left, right);
            eat(id);
            putDownChopsticks(left, right);
        }
    }

    private static void think(int id) {
        System.out.println("Philosopher " + id + " is thinking.");
        try {
            Thread.sleep(rand.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void pickUpChopsticks(int left, int right) {
        synchronized (chopsticks) {
            while (chopsticks[left] || chopsticks[right]) {
                try {
                    chopsticks.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            chopsticks[left] = true;
            chopsticks[right] = true;
        }
    }

    private static void eat(int id) {
        System.out.println("Philosopher " + id + " is eating.");
        try {
            Thread.sleep(rand.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void putDownChopsticks(int left, int right) {
        synchronized (chopsticks) {
            chopsticks[left] = false;
            chopsticks[right] = false;
            chopsticks.notifyAll();
        }
    }

    public static void main(String[] args) {
        Thread[] philosophers = new Thread[NUM_PHILOSOPHERS];
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            final int id = i;
            philosophers[i] = new Thread(() -> philosopher(id));
            philosophers[i].start();
        }
    }
}

