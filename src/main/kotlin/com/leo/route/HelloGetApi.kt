package com.leo.route

import com.leo.route.api.IGetRouterApi
import io.ktor.server.routing.*

class HelloGetApi(override val route: Route): IGetRouterApi() {

    override val name: String
        get() = "/hello"

    override suspend fun getData(): Map<String, String> {
        return mapOf("hello" to "leo")
    }
}