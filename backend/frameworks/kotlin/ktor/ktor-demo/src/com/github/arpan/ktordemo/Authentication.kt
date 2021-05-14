package com.github.arpan.ktordemo

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.authentication() {

    install(Authentication) {
        basic("auth") {
            realm = "Auth sample"
            validate { credentials ->
                // Logic to validate credentials
                if (credentials.name == credentials.password) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }

    routing {
        authenticate("auth") {
            get("/secure") {
                call.respondText { "Accessed secure area." }
            }
        }
    }
}