using System;
using System.Threading;

public class DiningPhilosophers
{
    static readonly int NUM_PHILOSOPHERS = 5;
    static bool[] chopsticks = new bool[NUM_PHILOSOPHERS];
    static Random rand = new Random();

    private static void Philosopher(object idObj)
    {
        int id = (int)idObj;
        int left = id;
        int right = (id + 1) % NUM_PHILOSOPHERS;

        while (true)
        {
            Think(id);
            PickUpChopsticks(left, right);
            Eat(id);
            PutDownChopsticks(left, right);
        }
    }

    private static void Think(int id)
    {
        Console.WriteLine("Philosopher " + id + " is thinking.");
        Thread.Sleep(rand.Next(1000));
    }

    private static void PickUpChopsticks(int left, int right)
    {
        lock (chopsticks)
        {
            while (chopsticks[left] || chopsticks[right])
            {
                Monitor.Wait(chopsticks);
            }
            chopsticks[left] = true;
            chopsticks[right] = true;
        }
    }

    private static void Eat(int id)
    {
        Console.WriteLine("Philosopher " + id + " is eating.");
        Thread.Sleep(rand.Next(1000));
    }

    private static void PutDownChopsticks(int left, int right)
    {
        lock (chopsticks)
        {
            chopsticks[left] = false;
            chopsticks[right] = false;
            Monitor.PulseAll(chopsticks);
        }
    }

    public static void Main(string[] args)
    {
        Thread[] philosophers = new Thread[NUM_PHILOSOPHERS];
        for (int i = 0; i < NUM_PHILOSOPHERS; i++)
        {
            philosophers[i] = new Thread(new ParameterizedThreadStart(Philosopher));
            philosophers[i].Start(i);
        }

        
        while (true)
        {
            Thread.Sleep(1000);
        }
    }
}
