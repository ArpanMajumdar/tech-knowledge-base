package com.github.arpan.ktordemo

import io.ktor.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // Simple text
    simple()

    // Same HTML and CSS
    html()

    // Working with custom configuration
    hocon()

    // Content negotiation, serializing response to JSON
    json()

    // Authentication
    authentication()

    // Status pages
    statusPages()
}


