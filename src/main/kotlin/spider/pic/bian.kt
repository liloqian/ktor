package spider.pic

import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import spider.net.Request
import java.io.File
import java.nio.charset.Charset

private const val ROOT_URL = "https://pic.netbian.com/"
private const val DOWNLOAD_ROOT_PATH = "src/main/resources/static/bian/"


data class CategoryHref(val href: String, val title: String, val text: String)
data class Wrapper(val url: String, val name: String, val page: Int, val index: Int)
data class CategoryData(val categoryHref: CategoryHref, val wrappers: List<Wrapper>)

fun startPicFromBiAn() {
    val client = Request()

    val categories = spiderCategories(client)

    val categoryDataList = spiderCategoryList(client, categories)

    downloadWrappers(client, categoryDataList)
}

/** first get categories */
fun spiderCategories(client: Request): List<CategoryHref> {
    val rootHtml = runBlocking {
        val str = String(client.get(ROOT_URL), Charset.forName("GBK"))
        Jsoup.parse(str)
    }
    val categories = mutableListOf<CategoryHref>()
    rootHtml.select("div.nav-m\\ clearfix\\ tran").first()?.children()?.forEach { element ->
        val href = element.attr("href") ?: ""
        val title = element.attr("title") ?: ""
        val text = element.text()
        if (href.isNotEmpty() && title.isNotEmpty() && text.isNotEmpty()) {
            categories.add(CategoryHref(href, title, text))
        } else {
            println("Exception: href:$href title:$title text:$text")
        }
    }
    println(categories)
    return categories
}

/** second get category data */
fun spiderCategoryList(client: Request, categories: List<CategoryHref>): List<CategoryData> {
    val categoryDataList = mutableListOf<CategoryData>()
    categories.forEach {
        val categoryHtml = runBlocking {
            val str = String(client.get(ROOT_URL + it.href), Charset.forName("GBK"))
            Jsoup.parse(str)
        }
        val wrappers = mutableListOf<Wrapper>()
        categoryHtml.select("div.slist").select("ul.clearfix").first()?.children()?.forEachIndexed { index, element ->
            val img = element.select("img")
            val url = img.attr("src") ?: ""
            val name = img.attr("alt") ?: ""
            if (url.isNotEmpty() && name.isNotEmpty()) {
                wrappers.add(Wrapper(url, name, 0, index))
            } else {
                println("Exception: url:$url name:$name $index")
            }
        }
        categoryDataList.add(CategoryData(it, wrappers))
    }
    println(categoryDataList)
    return categoryDataList
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
                client.downloadFile(ROOT_URL + wrapper.url, file)
            }
        }
    }
}

fun main() {
    startPicFromBiAn()
}