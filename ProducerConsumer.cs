using System;
using System.Collections.Generic;
using System.Threading;

class Program
{
    const int N = 100;
    int count = 0;
    Queue<int> queue = new Queue<int>();
    object lockObject = new object();
    AutoResetEvent producerEvent = new AutoResetEvent(false);
    AutoResetEvent consumerEvent = new AutoResetEvent(false);

    class ProducerConsumer
    {
        Program program;

        public ProducerConsumer(Program program)
        {
            this.program = program;
        }

        public void Produce()
        {
            while (true)
            {
                int item = program.ProduceItem();
                lock (program.lockObject)
                {
                    if (program.queue.Count == N)
                    {
                        program.producerEvent.WaitOne();
                    }
                    program.queue.Enqueue(item);
                    program.count++;
                    if (program.count == 1)
                    {
                        program.consumerEvent.Set();
                    }
                }
            }
        }

        public void Consume()
        {
            while (true)
            {
                int item;
                lock (program.lockObject)
                {
                    if (program.count == 0)
                    {
                        program.consumerEvent.WaitOne();
                    }
                    item = program.queue.Dequeue();
                    program.count--;
                    if (program.count == N - 1)
                    {
                        program.producerEvent.Set();
                    }
                }
                program.ConsumeItem(item);
            }
        }
    }

    int ProduceItem()
    {
        Random rnd = new Random();
        return rnd.Next(1000);
    }

    void ConsumeItem(int item)
    {
        Console.WriteLine($"The square is: {item * item}");
    }

    static void Main(string[] args)
    {
        Program program = new Program();
        ProducerConsumer pc = new ProducerConsumer(program);

        Thread producerThread = new Thread(pc.Produce);
        Thread consumerThread = new Thread(pc.Consume);

        producerThread.Start();
        consumerThread.Start();

        producerThread.Join();
        consumerThread.Join();
    }
}
