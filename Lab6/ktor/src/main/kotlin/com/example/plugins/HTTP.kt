package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.hsts.*

fun Application.configureHTTP() {
    install(HSTS) {
        includeSubDomains = true
    }
}
