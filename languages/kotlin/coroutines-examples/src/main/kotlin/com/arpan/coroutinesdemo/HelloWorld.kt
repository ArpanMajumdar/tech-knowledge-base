package com.arpan.coroutinesdemo

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun helloWorld(){
    GlobalScope.launch {
        delay(1000)
        print("World !")
    }
    print("Hello ")
    Thread.sleep(2000)
}

fun coroutinesAreLightWeight() = runBlocking {
    // Coroutines are light weight
    // We can spin millions of coroutines without any impact to the system
    repeat(1_000_000){
        launch {
            print(".")
        }
    }
}

fun main(){
    coroutinesAreLightWeight()
}