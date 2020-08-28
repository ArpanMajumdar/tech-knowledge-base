package com.github.arpan.multithreadingexamples.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InvokeAllDemo {

    private static final Logger logger = LoggerFactory.getLogger(InvokeAllDemo.class);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newWorkStealingPool();

        List<Callable<String>> callables = List.of(
            () -> {
                Thread.sleep(1000L);
                return "task1";
            },
            () -> {
                Thread.sleep(500L);
                return "task2";
            },
            () -> {
                Thread.sleep(2000L);
                return "task3";
            }
        );

        executorService
            .invokeAll(callables)
            .stream()
            .map(future -> {
                try {
                    return future.get();
                } catch (Exception ex) {
                    throw new IllegalStateException();
                }
            })
            .forEach(logger::info);
      
    }
}
