**ClassicIPC**

This repository contains various implementations of classic concurrency problems solved using different synchronization mechanisms in different programming languages.

### Problem Descriptions:

1. **Dining Philosophers Problem:**
    - Implemented in C#, Java, Kotlin, and C++.
    - The problem involves a group of philosophers sitting around a dining table with a bowl of spaghetti in the center and a chopstick between each pair of adjacent philosophers. 
    - Philosophers alternate between thinking and eating but can only eat if they can acquire both chopsticks.

2. **Peterson's Solution:**
    - Implemented in Java.
    - Peterson's solution is a concurrent programming algorithm for mutual exclusion that allows two processes to share a single-use resource without conflict, using only shared memory for communication.

3. **Producer-Consumer Problem:**
    - Implemented in C#, Java, Kotlin, and C++.
    - Involves two types of processes, producers and consumers, who share a common, fixed-size buffer used as a queue. 
    - Producers place items into the buffer, and consumers remove them. The problem is to make sure that the producer doesn't try to add items into the buffer if it's full and that the consumer doesn't try to remove items from an empty buffer.

4. **Semaphore Implementation:**
    - Implemented in C#, Java, and C++.
    - Semaphores are a classic synchronization primitive used to control access to a common resource by multiple processes in a concurrent system. They allow controlling access to shared resources through a counter mechanism.

### Usage:
- Each implementation can be compiled and executed using the respective programming language's compiler or interpreter.
- Detailed usage instructions are provided within each code file as comments or through method documentation.
