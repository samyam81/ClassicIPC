using System;
using System.Collections.Generic;
using System.Threading;

public class Semaphore
{
    long N = 100;
    int mutex = 1;
    int empty;
    int full = 0;
    Queue<int> linkedList = new Queue<int>();
    Random rand = new Random();

    public Semaphore()
    {
        empty = (int)N;
    }

    public static void Main(string[] args)
    {
        Semaphore sc = new Semaphore();
        Thread producer = new Thread(sc.Producer);
        Thread consumer = new Thread(sc.Consumer);
        producer.Start();
        consumer.Start();
    }

    public void Producer()
    {
        while (true)
        {
            int item = ProduceItem();
            try
            {
                Down(ref empty);
                Down(ref mutex);
                InsertItem(item);
                Up(ref mutex);
                Up(ref full);
            }
            catch (ThreadInterruptedException e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }

    public void Consumer()
    {
        while (true)
        {
            try
            {
                Down(ref full);
                Down(ref mutex);
                if (linkedList.Count > 0)
                {
                    int item = RemoveItem();
                    Up(ref mutex);
                    Up(ref empty);
                    ConsumeItem(item);
                }
                else
                {
                    Up(ref mutex);
                }
            }
            catch (ThreadInterruptedException e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }

    public int ProduceItem()
    {
        return rand.Next(100);
    }

    public void Down(ref int a)
    {
        lock (this)
        {
            while (a <= 0)
            {
                Monitor.Wait(this);
            }
            a--;
        }
    }

    public void Up(ref int a)
    {
        lock (this)
        {
            a++;
            Monitor.Pulse(this);
        }
    }

    public void InsertItem(int a)
    {
        linkedList.Enqueue(a);
    }

    public void ConsumeItem(int a)
    {
        Console.WriteLine("The square is: " + a * a);
    }

    public int RemoveItem()
    {
        return linkedList.Dequeue();
    }
}
