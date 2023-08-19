package com.wrapper

import com.app.route.api.IGetRouterApi
import io.ktor.server.routing.*
import java.io.File

class WallPaperRouteApi(override val route: Route) : IGetRouterApi() {

    override val name: String = "wrapper"

    override suspend fun getData(): List<CategoryData> {
        val rootFile = File(DOWNLOAD_ROOT_PATH)
        return if (rootFile.isDirectory) {
            val categories = mutableListOf<CategoryData>()
            rootFile.listFiles()?.forEach { parent ->
                val wrappers = mutableListOf<WallPaper>()
                parent.listFiles()?.forEach { wallPaper ->
                    wrappers.add(WallPaper(PIC_PREFIX_URL + parent.name + "/" + wallPaper.name, wallPaper.name))
                }
                categories.add(CategoryData(parent.name, wrappers))
            }
            categories
        } else {
            mutableListOf()
        }
    }
}

private const val DOWNLOAD_ROOT_PATH = "src/main/resources/static/bian/"
private const val PIC_PREFIX_URL = "http://127.0.0.1:8080/static/bian/"

data class WallPaper(val url: String, val name: String)
data class CategoryData(val name: String, val wrappers: List<WallPaper>)