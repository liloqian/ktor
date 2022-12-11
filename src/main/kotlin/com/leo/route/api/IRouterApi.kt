package com.leo.route.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

interface IRouterApi {

    val name: String

    val route: Route

    val method: Method

    suspend fun getData(): Any

    fun register() {
        if (method == Method.GET) {
            route.get(name) {
                hookCallBefore(call)
                call.respond(getData())
                hookCallAfter(call)
            }
        } else if(method == Method.POST) {
            route.post(name) {
                hookCallBefore(call)
                call.respond(getData())
                hookCallAfter(call)
            }
        }
    }

    fun hookCallAfter(call: ApplicationCall) {
    }

    fun hookCallBefore(call: ApplicationCall) {
    }
}