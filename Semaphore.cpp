#include <iostream>
#include <queue>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <random>

class Semaphore {
private:
    long N = 100;
    int mutex = 1;
    int empty;
    int full = 0;
    std::queue<int> linkedList;
    std::mutex mtx;
    std::condition_variable cv;
    std::random_device rd;
    std::mt19937 gen;
    std::uniform_int_distribution<int> dis;

public:
    Semaphore() : gen(rd()), dis(0, 99) {
        empty = N;
    }

    void producer() {
        while (true) {
            int item = produce_item();
            {
                std::unique_lock<std::mutex> lock(mtx);
                while (empty <= 0) {
                    cv.wait(lock);
                }
                empty--;
                mtx.unlock();
                insertItem(item);
                mtx.lock();
                full++;
                mtx.unlock();
                cv.notify_all();
            }
        }
    }

    void consumer() {
        while (true) {
            {
                std::unique_lock<std::mutex> lock(mtx);
                while (full <= 0) {
                    cv.wait(lock);
                }
                full--;
                mtx.unlock();
                int item = removeItem();
                mtx.lock();
                empty++;
                mtx.unlock();
                cv.notify_all();
                consumeItem(item);
            }
        }
    }

    int produce_item() {
        return dis(gen);
    }

    void insertItem(int a) {
        linkedList.push(a);
    }

    void consumeItem(int a) {
        std::cout << "The square is: " << a * a << std::endl;
    }

    int removeItem() {
        int front = linkedList.front();
        linkedList.pop();
        return front;
    }
};

int main() {
    Semaphore sem;
    std::thread producerThread(&Semaphore::producer, &sem);
    std::thread consumerThread(&Semaphore::consumer, &sem);
    producerThread.join();
    consumerThread.join();
    return 0;
}
