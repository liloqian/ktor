package spider.pic

import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import spider.net.Request
import java.io.File
import java.lang.Math.random
import java.nio.charset.Charset
import java.util.concurrent.Executors
import kotlin.coroutines.EmptyCoroutineContext

private const val ROOT_URL = "https://pic.netbian.com/"
private const val DOWNLOAD_ROOT_PATH = "src/main/resources/static/bian/"


data class CategoryHref(val href: String, val title: String, val text: String)
data class WallPaper(val url: String, val name: String, val page: Int, val index: Int)
data class CategoryData(val categoryHref: CategoryHref, val wrappers: List<WallPaper>)

val scope = CoroutineScope(EmptyCoroutineContext + Dispatchers.IO)

val executors = Executors.newFixedThreadPool(5).asCoroutineDispatcher()

fun startPicFromBiAn() {
    val client = Request()
    runBlocking {
        val categories = spiderCategories(client)

        val categoryDataList = spiderCategoryList(client, categories)

        downloadWrappers(client, categoryDataList)
    }
}

/** first get categories */
suspend fun spiderCategories(client: Request): List<CategoryHref> {
    val str = String(client.get(ROOT_URL), Charset.forName("GBK"))
    val categories = mutableListOf<CategoryHref>()
    Jsoup.parse(str).select("div.nav-m\\ clearfix\\ tran").first()?.children()?.forEach { element ->
        val href = element.attr("href") ?: ""
        val title = element.attr("title") ?: ""
        val text = element.text()
        if (href.isNotEmpty() && title.isNotEmpty() && text.isNotEmpty()) {
            categories.add(CategoryHref(href, title, text))
        } else {
            println("Exception: href:$href title:$title text:$text")
        }
    }
    if (categories.isEmpty()) {
        println("use cached root.html")
        Jsoup.parse(File("src/main/kotlin/spider/pic/root.html")).select("div.nav-m\\ clearfix\\ tran").first()?.children()?.forEach { element ->
            val href = element.attr("href") ?: ""
            val title = element.attr("title") ?: ""
            val text = element.text()
            if (href.isNotEmpty() && title.isNotEmpty() && text.isNotEmpty()) {
                categories.add(CategoryHref(href, title, text))
            } else {
                println("Exception: href:$href title:$title text:$text")
            }
        }
    }
    println(categories)
    return categories
}

/** second get category data */
suspend fun spiderCategoryList(client: Request, categories: List<CategoryHref>): List<CategoryData> {
    val categoryDataList = mutableListOf<CategoryData>()
    val jobs = mutableListOf<Deferred<Boolean>>()
    categories.forEach {
        val job = scope.async {
            val str = String(client.get(ROOT_URL + it.href), Charset.forName("GBK"))
            val wrappers = mutableListOf<WallPaper>()
            Jsoup.parse(str).select("div.slist").select("ul.clearfix").first()?.children()
                ?.forEachIndexed { index, element ->
                    val img = element.select("img")
                    var url = highResolutionImgUrl(client, element)
                    if (url.isNullOrEmpty()) {
                        url = img.attr("src") ?: ""
                    }
                    val name = img.attr("alt") ?: ""
                    if (url.isNotEmpty() && name.isNotEmpty()) {
                        wrappers.add(WallPaper(url, name, 0, index))
                    } else {
                        println("Exception: url:$url name:$name $index")
                    }
                }
            delay((random() * 1000).toLong())
            categoryDataList.add(CategoryData(it, wrappers))
        }
        jobs.add(job)
    }
    jobs.forEach {
        it.await()
    }
    println(categoryDataList)
    return categoryDataList
}

fun highResolutionImgUrl(client: Request, element: Element): String? {
    val imgDetailUrl = element.select("a").attr("href")
    val html = runBlocking {
        val str = String(client.get(ROOT_URL + imgDetailUrl), Charset.forName("GBK"))
        Jsoup.parse(str)
    }
    return html.select("div.photo-pic").select("img").attr("src")
}

/** download wrapper */
fun downloadWrappers(client: Request, categoryDataList: List<CategoryData>) {
    runBlocking {
        categoryDataList.forEach {
            val categoryFile = File(DOWNLOAD_ROOT_PATH + it.categoryHref.text)
            if (!categoryFile.exists()) {
                categoryFile.mkdirs()
            }
            it.wrappers.forEach { wrapper ->
                val file = File(categoryFile, wrapper.name + ".png")
                if (!file.exists()) {
                    file.createNewFile()
                }
                println("download name:${wrapper.name} url:${wrapper.url}")
                client.downloadFile(ROOT_URL + wrapper.url, file)
                delay((random() * 1000).toLong())
            }
        }
    }
}

fun main() {
    startPicFromBiAn()
}