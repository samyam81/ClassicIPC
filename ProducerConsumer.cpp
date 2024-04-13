#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <deque>
#include <random>

using namespace std;

class ProducerConsumer {
private:
    const long N = 100;
    int count = 0;
    deque<int> deque;
    mutex mtx;
    condition_variable cv;

public:
    void run() {
        Producer producer;
        Consumer consumer;

        thread producerThread(&Producer::produce, &producer);
        thread consumerThread(&Consumer::consume, &consumer);

        producerThread.join();
        consumerThread.join();
    }

    class Producer {
    public:
        void produce() {
            while (true) {
                int item = produce_item();
                {
                    unique_lock<mutex> lock(mtx);
                    if (deque.size() == N) {
                        cv.wait(lock);
                    }
                    insert(item);
                    count++;
                    if (count == 1) {
                        cv.notify_one();
                    }
                }
            }
        }

        int produce_item() {
            random_device rd;
            mt19937 gen(rd());
            uniform_int_distribution<int> dis(0, 999);
            return dis(gen);
        }

        void insert(int a) {
            deque.push_back(a);
        }
    };

    class Consumer {
    public:
        void consume() {
            while (true) {
                int item;
                {
                    unique_lock<mutex> lock(mtx);
                    if (count == 0) {
                        cv.wait(lock);
                    }
                    item = remove();
                    count--;
                    if (count == N - 1) {
                        cv.notify_one();
                    }
                }
                consume_item(item);
            }
        }

        void consume_item(int item) {
            cout << "the square is::::::" << item * item << endl;
        }

        int remove() {
            int front = deque.front();
            deque.pop_front();
            return front;
        }
    };
};

int main() {
    ProducerConsumer pc;
    pc.run();
    return 0;
}
