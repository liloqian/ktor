package com.app.route.api

import io.ktor.http.*

sealed class Method(name: String) {

    data object HEAD : Method("head")

    data object GET : Method("get")

    data object POST : Method("post")
}

fun HttpMethod.toMethod(): Method {
    if (value == "GET") {
        return Method.GET
    }
    if (value == "POST") {
        return Method.POST
    }
    if (value == "HEAD") {
        return Method.HEAD
    }
    return Method.GET
}