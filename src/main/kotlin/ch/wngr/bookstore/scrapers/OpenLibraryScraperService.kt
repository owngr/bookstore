package ch.wngr.bookstore.scrapers

import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.repositories.AuthorRepository
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.http4k.client.ApacheClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OpenLibraryScraperService : ScraperInterface {
    val OPEN_LIBRARY_API_URL = "https://openlibrary.org/api/books"
    var client: HttpHandler = ApacheClient()

    override fun getBookInfo(isbn: String): ScraperBook {
        val request = Request(Method.GET, OPEN_LIBRARY_API_URL)
            .query("bibkeys","ISBN:$isbn")
            .query("jscmd", "details")
            .query("format", "json")
        try {
            val response = client(request)
            val obj = JSONObject(response.body.toString())
            var volumeInfo = JSONObject()
            try {
                volumeInfo = (obj["ISBN:$isbn"] as JSONObject)["details"] as JSONObject
            } catch (e: JSONException) {
                println("openlibrary: book not found")
            }

            var coverUrl = ""
            try {
                coverUrl = (obj["ISBN:$isbn"] as JSONObject)["thumbnail_url"] as String
                // we want the large cover
                coverUrl = coverUrl.replace("S.jpg", "L.jpg")
            } catch (e: JSONException) {
                println("openlibrary: cover not found")
            }


            var title = ""
            try {
                title = volumeInfo["title"] as String
            } catch (e: JSONException) {
                println("openlibrary: title not found")
            }
            var publisher = ""
            try {
                publisher = (volumeInfo["publishers"] as JSONArray)[0] as String
            } catch (e: JSONException) {
                println("openlibrary: publisher not found")
            }
            var description = ""
            try {
                description = (volumeInfo["description"] as JSONObject)["value"] as String
            } catch (e: JSONException) {
                println("openlibrary: description not found")
            }
            var authors = ArrayList<String>()
            try {
                authors = authorsJsonArrayToStringList(volumeInfo["authors"] as JSONArray)
            } catch (e: JSONException) {
                println("openlibrary: authors not found")
            }
            
            return ScraperBook(
                isbn = isbn,
                title = title,
                authors = authors,
                editor = publisher,
                distributor = "",
                description = description,
                price = null,
                coverUrl = coverUrl,
                hasCover = coverUrl != "",
                amount = null,
                tags = ArrayList(),
            )
        } catch (e: Exception) {
            println(e)
            throw e
        }
    }

    companion object{
        fun authorsJsonArrayToStringList(jsonArray: JSONArray): ArrayList<String> {
            val list: ArrayList<String> = arrayListOf()
            for (i in 0 until jsonArray.length()) {
                list.add((jsonArray[i] as JSONObject)["name"] as String)
            }
            return list
        }
    }

}