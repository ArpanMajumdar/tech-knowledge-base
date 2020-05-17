package com.arpan.coroutinesdemo.exceptionhandling

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("ExceptionHandling")

    runBlocking {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            logger.error("An exception occurred: ${throwable.localizedMessage}")
        }
        val job = GlobalScope.launch(exceptionHandler) {
            logger.info("Throwing exception from job")
            throw ArrayIndexOutOfBoundsException("Index out of bounds")
        }
        job.join()

        val deferred = GlobalScope.async {
            logger.info("Throwing exception from async")
            throw ArithmeticException("Divide by zero")
        }


        deferred.await()

    }
}