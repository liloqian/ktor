package com.leo.route

import com.leo.model.Person
import com.leo.route.api.IGetRouterApi
import io.ktor.server.routing.*

class PersonGetApi(override val route: Route) : IGetRouterApi() {

    override val name: String
        get() = "/person"

    override suspend fun getData(): List<Person> {
        return mutableListOf<Person>().apply {
            add(Person("hello", 18, true))
            add(Person("hello1", 18, true))
            add(Person("hello1", 18, true))
            add(Person("hello1", 18, true))
            add(Person("hello2", 18, true))
        }
    }
}