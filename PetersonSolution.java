import java.util.Random;

public class PetersonSolution {
    // The code is for the implementation of Peterson's solution.
    
    static boolean[] flag = {false, false}; // Flags for each process
    static int turn = 0; // Indicate whose turn it is to enter
    static Random rand = new Random();

    private static void processP0() {
        flag[0] = true;
        turn = 1;
        while (flag[1] && turn == 1) {
            try {
                Thread.sleep(rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        create();
        flag[0] = false;
    }

    private static void create() {
        int random = rand.nextInt(19);
        System.out.println("The square is: " + random * random);
    }

    private static void processP1() {
        flag[1] = true;
        turn = 0;
        while (flag[0] && turn == 0) {
            try {
                Thread.sleep(rand.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        create2();
        flag[1] = false;
    }

    private static void create2() {
        int random = rand.nextInt(19);
        System.out.println("The cube is: " + random * random * random);
    }

    public static void main(String[] args) {
        Thread p0 = new Thread(PetersonSolution::processP0);
        Thread p1 = new Thread(PetersonSolution::processP1);

        p0.start();
        p1.start();
    }
}
