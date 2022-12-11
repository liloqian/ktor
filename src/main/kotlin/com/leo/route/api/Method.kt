package com.leo.route.api

import io.ktor.http.*

sealed class Method(name: String) {

    object HEAD : Method("head")

    object GET : Method("get")

    object POST : Method("post")
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