package spider.net

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import java.io.File


class Request {

    private val client by lazy {
        HttpClient(CIO)
    }

    suspend fun get(url: String): ByteArray {
         return client.get(url).body()
    }

    suspend fun post(url: String) {
        client.post(url)
    }

    suspend fun downloadFile(url: String, file: File) {
        client.get(url).bodyAsChannel().toInputStream().copyTo(file.outputStream())
    }

}