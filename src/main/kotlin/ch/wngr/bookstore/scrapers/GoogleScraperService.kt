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
class GoogleScraperService : ScraperInterface {
    val GOOGLE_API_URL = "https://www.googleapis.com/books/v1/volumes"
    var client: HttpHandler = ApacheClient()

    override fun getBookInfo(isbn: String): ScraperBook {
        val request = Request(Method.GET, GOOGLE_API_URL).query("q", "isbn:$isbn")
        try {

            val response = client(request)
            val obj = JSONObject(response.body.toString())
            var volumeInfo = JSONObject()
            try {
                volumeInfo = (obj["items"] as JSONArray).getJSONObject(0)["volumeInfo"] as JSONObject
            } catch (e: JSONException) {
                println("google: book not found")
            }
            var coverUrl = ""
            try {
                coverUrl = (volumeInfo["imageLinks"] as JSONObject)["thumbnail"] as String
                coverUrl = coverUrl.replace("http:", "https:")
            } catch (e: JSONException) {
                println("Google: cover not found")
            }

            var title = ""
            try {
                title = volumeInfo["title"] as String
            } catch (e: JSONException) {
                println("google: title not found")
            }
            var publisher = ""
            try {
                publisher = volumeInfo["publisher"] as String
            } catch (e: JSONException) {
                println("google: publisher not found")
            }
            var description = ""
            try {
                description = volumeInfo["description"] as String
            } catch (e: JSONException) {
                println("google: description not found")
            }
            var authors = ArrayList<String>()
            try {
                authors = jsonArrayToStringList(volumeInfo["authors"] as JSONArray)
            } catch (e: JSONException) {
                println("google: authors not found")
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

    companion object {
        fun jsonArrayToStringList(jsonArray: JSONArray): ArrayList<String> {
            val list: ArrayList<String> = arrayListOf()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray[i] as String)
            }
            return list
        }
    }
}
