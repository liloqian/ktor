package com.book.route

import com.book.model.Person
import com.app.route.api.IPostRouteApi
import io.ktor.server.request.*
import io.ktor.server.routing.*

class PersonPostApi(override val route: Route) : IPostRouteApi() {

    override val name: String
        get() = "person"

    override suspend fun getData(): List<Person> {
        return request.call.receive()
    }
}