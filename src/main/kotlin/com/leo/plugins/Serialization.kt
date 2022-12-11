package com.leo.plugins

import com.leo.route.BookGetPostApi
import com.leo.route.HelloGetApi
import com.leo.route.PersonGetApi
import com.leo.route.PersonPostApi
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        PersonGetApi(this).register()
        HelloGetApi(this).register()
        PersonPostApi(this).register()
        BookGetPostApi(this).register()
    }
}




