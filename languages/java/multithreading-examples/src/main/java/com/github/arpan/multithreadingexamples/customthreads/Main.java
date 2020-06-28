package com.github.arpan.multithreadingexamples.customthreads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // By extending Thread class
        Thread t1 = new HelloThread();

        // By  implementing runnable interface
        Thread t2 = new Thread(new HelloRunnable());

        // By passing a lambda function to thread class
        Thread t3 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error("Thread interrupted: {}", e);
            }
            logger.info("Hello world from lambda");
        });

        logger.info("Starting threads ...");
        t1.start();
        t2.start();
        t3.start();
        logger.info("Finished");
    }
}
