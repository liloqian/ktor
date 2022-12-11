package com.leo.route

import com.leo.model.Person
import com.leo.route.api.IPostRouteApi
import io.ktor.server.request.*
import io.ktor.server.routing.*

class PersonPostApi(override val route: Route) : IPostRouteApi() {

    override val name: String
        get() = "person"

    override suspend fun getData(): List<Person> {
        return request.call.receive()
    }
}