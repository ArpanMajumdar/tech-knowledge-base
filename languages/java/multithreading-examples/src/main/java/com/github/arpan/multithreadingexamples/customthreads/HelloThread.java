package com.github.arpan.multithreadingexamples.customthreads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(HelloThread.class);

    @Override
    public void run() {
        logger.info("Hello world from Hello Thread");
    }
}
