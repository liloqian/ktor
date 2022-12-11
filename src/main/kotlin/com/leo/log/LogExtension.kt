package com.leo.log

import io.ktor.server.request.*
import io.ktor.util.*

fun logRequest(request: ApplicationRequest) {
    logRed("---------------request log----------------")
    logRed("uri: ${request.uri}")
    logRed("method: ${request.httpMethod.value}")
    logRed("parameter: ${request.queryParameters.toMap()}")
    logRed("header: ${request.headers.toMap()}")
    clear("")
}


fun logRed(str: String) {
    println("\u001b[0;31;48m$str")
}

fun clear(str: String) {
    println("\u001b[0;310;48m$str")
}