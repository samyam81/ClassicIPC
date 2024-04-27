import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

class Program {
    companion object {
        const val N = 100
    }

    var count = 0
    val queue: Queue<Int> = LinkedList()
    val lockObject = ReentrantLock()
    val producerEvent = java.util.concurrent.Semaphore(0)
    val consumerEvent = java.util.concurrent.Semaphore(0)

    inner class ProducerConsumer {
        fun produce() {
            while (true) {
                val item = produceItem()
                lockObject.lock()
                if (queue.size == N) {
                    lockObject.unlock()
                    producerEvent.acquire()
                    lockObject.lock()
                }
                queue.add(item)
                count++
                if (count == 1) {
                    consumerEvent.release()
                }
                lockObject.unlock()
            }
        }

        fun consume() {
            while (true) {
                lockObject.lock()
                if (count == 0) {
                    lockObject.unlock()
                    consumerEvent.acquire()
                    lockObject.lock()
                }
                val item = queue.remove()
                count--
                if (count == N - 1) {
                    producerEvent.release()
                }
                lockObject.unlock()
                consumeItem(item)
            }
        }
    }

    fun produceItem(): Int {
        return (0..999).random()
    }

    fun consumeItem(item: Int) {
        println("The square is: ${item * item}")
    }
}

fun main() {
    val program = Program()
    val pc = program.ProducerConsumer()

    val producerThread = thread(start = true) { pc.produce() }
    val consumerThread = thread(start = true) { pc.consume() }

    producerThread.join()
    consumerThread.join()
}
