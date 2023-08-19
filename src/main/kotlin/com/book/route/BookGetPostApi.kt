package com.book.route

import com.book.model.Book
import com.app.route.api.IGetPostRouteApi
import com.app.route.api.Method
import com.app.route.api.toMethod
import io.ktor.server.request.*
import io.ktor.server.routing.*

class BookGetPostApi(override val route: Route) : IGetPostRouteApi() {

    override val name: String
        get() = "book"

    override val method: Method
        get() = request.httpMethod.toMethod()

    override suspend fun getData(): List<Book> {
        return if (method != Method.POST) {
            mutableListOf<Book>().apply {
                for(i in 0..5) {
                    add(Book("岛上书店$i", "leo", 18.0f))
                }
            }
        } else {
            request.call.receive<List<Book>>().subList(0, 2)
        }
    }
}