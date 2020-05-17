package com.arpan.coroutinesdemo.job

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main() {
    val logger: Logger = LoggerFactory.getLogger("JobsDemo")

    runBlocking {
        val job1 = launch {
            logger.info("Job 1 launched")
            val job11 = launch {
                logger.info("Job 11 launched")
                delay(3000L)
                logger.info("Job 11 is completing ...")
            }
            job11.invokeOnCompletion {
                logger.info("Job 11 completed")
            }

            val job12 = launch {
                logger.info("Job 12 launched")
                delay(3000L)
                logger.info("Job 12 is completing ...")
            }
            job12.invokeOnCompletion {
                logger.info("Job 12 completed")
            }

            delay(3000L)
            logger.info("Job 1 is completing ...")
        }
        job1.invokeOnCompletion {
            logger.info("Job 1 completed")
        }

        delay(500L)
        logger.info("Canceling Job 1")
        job1.cancel()
    }
}