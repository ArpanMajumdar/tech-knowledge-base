package com.github.arpan.multithreadingexamples.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ExecutorsDemo {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorsDemo.class);

    public static void main(String[] args) {
        int numThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        Runnable runnableTask = () -> logger.info("Hello from runnable task from thread {}", Thread.currentThread().getName());

        Callable<Integer> callableTask = () -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                logger.error("callableTask interrupted: {}", ex);
                throw ex;
            }
            logger.info("Hello from callable task from thread {}", Thread.currentThread().getName());
            return 123;
        };

        // Runnable task example
        executorService.submit(runnableTask);

        // Callable task example
        Future<Integer> future = executorService.submit(callableTask);
        logger.info("Checking is future is completed? {}", future.isDone());
        Integer result = null;
        int timeoutInSec = 2;
        try {
            result = future.get(timeoutInSec, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Error occurred: {}",e);
        }
        logger.info("Checking is future is completed? {}", future.isDone());
        logger.info("Result: {}", result);
        shutdownExecutor(executorService);
    }

    private static void shutdownExecutor(ExecutorService executorService) {
        try {
            logger.info("Shutting down executor ...");
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.error("Tasks interrupted");
        } finally {
            if (!executorService.isTerminated()) {
                logger.info("Executor still running after 5 seconds. Attempting force shutdown ...");
            }
            executorService.shutdownNow();
            logger.info("Shutdown finished.");
        }
    }
}
