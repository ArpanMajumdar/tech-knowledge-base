package com.github.arpan.ktordemo

import io.ktor.application.*
import io.ktor.config.*

operator fun ApplicationConfig.get(key: String): String? = this.propertyOrNull(key)?.getString()

fun Application.hocon() {
    val custom = environment.config.config("ktor.custom")
    val key1 = custom["key1"]
    val key2 = custom["key2"]

    log.info("key1 => $key1")
    log.info("key2 => $key2")
}