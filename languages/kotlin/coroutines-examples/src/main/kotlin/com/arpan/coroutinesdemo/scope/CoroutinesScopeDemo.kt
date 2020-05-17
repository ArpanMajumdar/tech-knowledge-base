package com.arpan.coroutinesdemo.scope

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("CoroutinesScopeDemo")

    logger.info("Program execution will now block ...")
    runBlocking {
        launch {
            delay(1000L)
            logger.info("Task from local scope")
        }

        GlobalScope.launch {
            delay(500L)
            logger.info("Task from global scope")
        }

        coroutineScope {
            launch {
                delay(1500L)
                logger.info("Task from new coroutine scope")
            }
        }
    }
    logger.info("Program execution will now continue.")
}