package com.app.route.api

import com.app.log.logRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

abstract class IGetPostRouteApi : IRouterApi {

    lateinit var request: ApplicationRequest

    override fun register() {
        route.route(name) {
            get {
                dealCall(call)
            }
            post {
                dealCall(call)
            }
        }
    }

    private suspend fun dealCall(call: ApplicationCall) {
        hookCallBefore(call)
        call.respond(getData())
        hookCallAfter(call)
    }

    override fun hookCallBefore(call: ApplicationCall) {
        super.hookCallBefore(call)
        request = call.request
        logRequest(request)
    }

}