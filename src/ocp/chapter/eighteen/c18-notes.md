# Chapter 18 - Concurrency

Concurrency:

- Create worker threads using Runnable, Callable and use an ExecutorService to concurrently execute tasks.
- Use java.util.concurrent collections and classes including CyclicBarrier and CopyOnWriteArrayList.
- Write thread-safe code.
- Identify threading problems such as deadlocks and livelocks.

Parallel Streams:

- Develop Code that uses parallel streams.
- Implement decomposition and reduction with streams.

## Introducing Threads (p.841-848)

Computers are capable of reading and writing data to external resources, unfortunately, as compared to CPU operations, these disk/network operations tend to be extremely slow, so slow that if your computer's operating system were to stop and wait for every disk or network operation to finish, your computer would appear to freeze or lock up constantly.
Luckily, all modern operating systems support what is known as multithreaded processing. The idea behind multithreaded processing is to allow an application or group of applications to execute multiple tasks at the same time. This allows tasks waiting for other resources to give way to other processing requests.

> **Note:** Previous Java certifications exams expected you to know details about threads, such as thread life cycles. But now the exam instead covers the basics of threads but focuses more on your knowledge of the Concurrency API.
