# Multithreading

- Each thread is represented by an object that is an instance of the java.lang.Thread class (or its subclass).
- A daemon thread (comes from UNIX terminology) is a low priority thread that runs in the background to perform tasks such as garbage collection and so on. JVM does not wait for daemon threads before exiting while it waits for non-daemon threads.
- You must invoke start if you'd like to execute your code inside run in another thread. If you invoke run directly, the code will be executed in the same thread.
- By default, a new thread is running in non-daemon mode. Reminder: the difference between the daemon and the non-daemon mode is that JVM will not terminate the running program while there're still non-daemon threads left, while the daemon threads won't prevent JVM from terminating.

## Executors

- The Concurrency API introduces the concept of an `ExecutorService` as a higher level replacement for working with threads directly. 
- Executors are capable of running asynchronous tasks and typically manage a pool of threads, so we don't have to create new threads manually. All threads of the internal pool will be reused under the hood for revenant tasks, so we can run as many concurrent tasks as we want throughout the life-cycle of our application with a single executor service.
- The class `Executors` provides convenient factory methods for creating different kinds of executor services.
- There is an important difference: the java process never stops! Executors have to be stopped explicitly - otherwise they keep listening for new tasks.
