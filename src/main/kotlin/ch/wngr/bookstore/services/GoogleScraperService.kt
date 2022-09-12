package ch.wngr.bookstore.services

import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.repositories.AuthorRepository
import khttp.responses.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GoogleScraperService : ScraperService {
    @field:Autowired
    private lateinit var authorRepository: AuthorRepository
    val GOOGLE_API_URL = "https://www.googleapis.com/books/v1/volumes"

    override fun getBookInfo(isbn: String): ScraperBook {
        try {
            val response: Response = khttp.get(
                url = GOOGLE_API_URL,
                params = mapOf("q" to "isbn:$isbn")
            )
            val obj: JSONObject = response.jsonObject
            var volumeInfo = JSONObject()
            try {
                volumeInfo = (obj["items"] as JSONArray).getJSONObject(0)["volumeInfo"] as JSONObject
            } catch (e: JSONException) {
                println("book not found")
            }
            var coverUrl = ""
            try {
                coverUrl = (volumeInfo["imageLinks"] as JSONObject)["thumbnail"] as String
                coverUrl = coverUrl.replace("http:", "https:")
            } catch (e: JSONException) {
                println("cover not found")
            }

            val title: String = volumeInfo["title"] as String
            var publisher = ""
            try {
                publisher = volumeInfo["publisher"] as String
            } catch (e: JSONException) {
                println("publisher not found")
            }
            var description = ""
            try {
                description = volumeInfo["description"] as String
            } catch (e: JSONException) {
                println("description not found")
            }
            val authors: List<String> = jsonArrayToStringList(volumeInfo["authors"] as JSONArray)
            return ScraperBook(
                isbn = isbn,
                title = title,
                authors = authors,
                editor = publisher,
                distributor = "",
                description = description,
                price = null,
                coverUrl = coverUrl,
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
