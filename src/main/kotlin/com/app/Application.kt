package com.app

import com.app.plugins.configureHTTP
import com.app.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = CONFIG_PORT, host = CONFIG_HOST, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureHTTP()
}

const val CONFIG_HOST = "192.168.1.18"
const val CONFIG_PORT = 8888
