package com.arpan.coroutinesdemo.context

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("ContextDemo")

    runBlocking {
        launch(CoroutineName("my-coroutine-1")) {
            logger.info("This is run from ${coroutineContext[CoroutineName.Key]}")
        }
    }
}