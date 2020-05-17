package com.arpan.coroutinesdemo.asyncawait

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.random.Random

fun main() {
    val logger: Logger = LoggerFactory.getLogger("AsyncAwaitDemo")

    runBlocking {
        val firstValDeferred = async { getFirstValue() }
        val secondValDeferred = async { getSecondValue() }

        // Perform some computation
        logger.info("Performing some computation")
        delay(1000L)

        logger.info("Computation completed.")
        logger.info("Waiting for values ...")

        val firstValue = firstValDeferred.await()
        val secondValue = secondValDeferred.await()

        logger.info("First value: $firstValue")
        logger.info("Second value: $secondValue")
        logger.info("Total: ${firstValue + secondValue}")
    }
}

suspend fun getFirstValue(): Int {
    delay(1000L)
    return Random.nextInt(100)
}

suspend fun getSecondValue(): Int {
    delay(2000L)
    return Random.nextInt(1000)
}