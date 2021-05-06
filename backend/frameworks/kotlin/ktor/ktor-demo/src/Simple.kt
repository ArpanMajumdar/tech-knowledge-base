package com.github.arpan

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.simple() {
    routing {
        get("/") {
            call.respondText("Hello world", ContentType.Text.Plain)
        }
    }
}