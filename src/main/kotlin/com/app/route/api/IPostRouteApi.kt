package com.app.route.api

import com.app.log.logRequest
import io.ktor.server.application.*
import io.ktor.server.request.*

abstract class IPostRouteApi: IRouterApi {

    lateinit var request: ApplicationRequest

    override val method: Method
        get() = Method.POST

    override fun hookCallBefore(call: ApplicationCall) {
        super.hookCallBefore(call)
        request = call.request
        logRequest(request)
    }
}