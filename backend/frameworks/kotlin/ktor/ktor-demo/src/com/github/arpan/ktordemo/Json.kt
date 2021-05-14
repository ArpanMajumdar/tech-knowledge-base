package com.github.arpan.ktordemo

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.Serializable

@Serializable
data class Customer(val id: Int, val name: String, val email: String)

fun Application.json() {
    install(ContentNegotiation) {
        json() // For kotlinx.serialization
    }

    routing {
        get("/customer") {
            val model = Customer(id = 1, name = "Arpan Majumdar", email = "arpan.gvdn7@gmail.com")
            call.respond(model)
        }
    }
}