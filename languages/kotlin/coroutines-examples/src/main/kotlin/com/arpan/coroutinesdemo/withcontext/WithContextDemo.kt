package com.arpan.coroutinesdemo.withcontext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main() {
    val logger: Logger = LoggerFactory.getLogger("WithContextDemo")

    runBlocking {
        launch(Dispatchers.Default) {
            logger.info("First context: $coroutineContext")

            withContext(Dispatchers.IO) {
                logger.info("Second context: $coroutineContext")
            }

            logger.info("Third context: $coroutineContext")
        }
    }
}