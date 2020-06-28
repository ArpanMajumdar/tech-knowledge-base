package com.github.arpan.multithreadingexamples.customthreads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HelloRunnable.class);

    @Override
    public void run() {
        logger.info("Hello world from Hello Runnable");
    }
}
