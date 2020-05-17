package com.arpan.coroutinesdemo.dispatchers

import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory


fun getThreadName(): String = Thread.currentThread().name


fun main() {
    val logger: Logger = LoggerFactory.getLogger("DispatchersDemo")

    runBlocking {
        // This is only meant for android. Will throw error for non-android applications

//        launch(Dispatchers.Main) {
//            logger.info("Dispatcher: Main, Thread: ${getThreadName()}")
//        }

        launch(Dispatchers.Unconfined) {
            logger.info("Dispatcher: Unconfined, Thread: ${getThreadName()}")
            delay(500L)
            logger.info("Dispatcher: Unconfined2, Thread: ${getThreadName()}")
        }

        launch(Dispatchers.Default) {
            logger.info("Dispatcher: Default, Thread: ${getThreadName()}")
        }

        launch(Dispatchers.IO) {
            logger.info("Dispatcher: IO, Thread: ${getThreadName()}")
        }

        launch(newSingleThreadContext("my-thread-1")) {
            logger.info("Dispatcher: newSingleThreadContext, Thread: ${getThreadName()}")
        }
    }
}