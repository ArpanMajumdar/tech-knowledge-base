package com.github.arpan.multithreadingexamples.threadclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadClassDemo {
    private static final Logger logger = LoggerFactory.getLogger(ThreadClassDemo.class);

    public static void main(String[] args) {
        Thread thread = Thread.currentThread();

        logger.info("Name: {}", thread.getName());
        logger.info("ID: {}", thread.getId());
        logger.info("Alive: {}", thread.isAlive());
        logger.info("Priority: {}", thread.getPriority());
        logger.info("Daemon: {}", thread.isDaemon());

        thread.setName("my-thread");
        logger.info("New name: {}", thread.getName());
        logger.info("Min thread priority: {}", Thread.MIN_PRIORITY);
        logger.info("Max thread priority: {}", Thread.MAX_PRIORITY);
    }
}
