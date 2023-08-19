package com.app.plugins

import com.book.route.BookGetPostApi
import com.book.route.HelloGetApi
import com.book.route.PersonGetApi
import com.book.route.PersonPostApi
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson {}
    }

    routing {
        /** hello world */
        get("/") {
            call.respond("hello world for come here :)")
        }

        /** retrofit book */
        PersonGetApi(this).register()
        HelloGetApi(this).register()
        PersonPostApi(this).register()
        BookGetPostApi(this).register()
    }
}




