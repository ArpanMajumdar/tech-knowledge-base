# Multithreading

- Each thread is represented by an object that is an instance of the java.lang.Thread class (or its subclass).
- A daemon thread (comes from UNIX terminology) is a low priority thread that runs in the background to perform tasks such as garbage collection and so on. JVM does not wait for daemon threads before exiting while it waits for non-daemon threads.
- You must invoke start if you'd like to execute your code inside run in another thread. If you invoke run directly, the code will be executed in the same thread.
- By default, a new thread is running in non-daemon mode. Reminder: the difference between the daemon and the non-daemon mode is that JVM will not terminate the running program while there're still non-daemon threads left, while the daemon threads won't prevent JVM from terminating.