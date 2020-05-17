package com.arpan.coroutinesdemo.suspendfunctions

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("SuspendFunctionsDemo")
var functionCalls = 0

fun main() {
    GlobalScope.launch { completeMessage() }
    GlobalScope.launch { improveMessage() }
    logger.info("Hello ")
    Thread.sleep(2000L)
    logger.info("There have been $functionCalls calls so far.")
}

suspend fun completeMessage() {
    delay(500L)
    logger.info("World !")
    functionCalls++
}

suspend fun improveMessage() {
    delay(1000L)
    logger.info("Suspend functions are cool stuff")
    functionCalls++
}