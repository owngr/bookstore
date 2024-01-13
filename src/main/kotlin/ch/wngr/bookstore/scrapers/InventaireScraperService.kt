package ch.wngr.bookstore.scrapers

import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.repositories.AuthorRepository
import net.bytebuddy.asm.Advice.Return
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
class InventaireScraperService: ScraperInterface {
    val INVENTAIRE_API_URL = "https://inventaire.io"
    var client: HttpHandler = ApacheClient()

    override fun getBookInfo(isbn: String): ScraperBook {
        val request = Request(Method.GET, INVENTAIRE_API_URL + "/api/entities")
            .query("action", "by-uris")
            .query("uris", "isbn:$isbn")
        try {
            val response = client(request)
            val obj = JSONObject(response.body.toString())
            var volumeInfo = JSONObject()
            try {
                volumeInfo = (obj["entities"] as JSONObject)["isbn:$isbn"] as JSONObject
            } catch (e: JSONException) {
                println("inventaire: book not found")
            }

            var coverUrl = ""
            try {
                coverUrl = INVENTAIRE_API_URL + ((volumeInfo["image"] as JSONObject)["url"] as String)
            } catch (e: JSONException) {
                println("inventaire: cover not found")
            }

            var title = ""
            try {
                title = (((volumeInfo["claims"] as JSONObject)["wdt:P1476"]) as JSONArray)[0] as String
            } catch (e: JSONException) {
                println("inventaire: title not found")
            }

            var publisher = ""
            try {
                var publisherEntityId = (((volumeInfo["claims"] as JSONObject)["wdt:P123"]) as JSONArray)[0] as String
                publisher = getPublisher(publisherEntityId)
            } catch (e: Exception) {
                println("inventaire: could not find publisher")
            }

            var description = ""
            try {
                description = getDescription(isbn)
            } catch (e: Exception) {
                println("inventaire: could not find description")
            }

            var authors = ArrayList<String>()
            try {
                var workEntityId = (((volumeInfo["claims"] as JSONObject)["wdt:P629"]) as JSONArray)[0] as String
                authors = getAuthors(workEntityId)
            } catch (e: Exception) {
                println("inventaire could not find authors")
            }

            println(coverUrl)
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

    fun getAuthors(entityId: String): ArrayList<String> {
        val request = Request(Method.GET, INVENTAIRE_API_URL + "/api/entities")
            .query("action", "by-uris")
            .query("uris", entityId)
        var authorsEntityId = ArrayList<String>()
        var authors = ArrayList<String>()
        try {
            val response = client(request)
            val obj = JSONObject(response.body.toString())
            var volumeInfo = JSONObject()
            try {
                volumeInfo = (obj["entities"] as JSONObject)[entityId] as JSONObject
            } catch (e: JSONException) {
                println("inventaire: work entity not found")
            }
            try {
                val authorsJSONArray = (volumeInfo["claims"] as JSONObject)["wdt:P50"] as JSONArray
                authorsEntityId = authorsJSONArray.map { e -> e as String } as ArrayList<String>
            } catch (e: JSONException) {
                println("inventaire: author entity id not found")
            }
        } catch (e: Exception) {
            println(e)
            throw e
        }
        for (authorEntityId in authorsEntityId) {
            val authorRequest = Request(Method.GET, INVENTAIRE_API_URL + "/api/entities")
                .query("action", "by-uris")
                .query("uris", authorEntityId)
            try {
                val response = client(authorRequest)
                val obj = JSONObject(response.body.toString())
                var volumeInfo = JSONObject()
                try {
                    volumeInfo = (obj["entities"] as JSONObject)[authorEntityId] as JSONObject
                } catch (e: JSONException) {
                    println("inventaire: author not found")
                }
                var author: String
                try {
                    author = (volumeInfo["labels"] as JSONObject)["fr"] as String
                    authors.add(author)
                } catch (e: JSONException) {
                    println("inventaire: could not find french label for author")
                }
            } catch (e: Exception) {
                println(e)
                throw e
            }
        }
        return authors
    }

    fun getPublisher(entityId: String): String {
        var publisher: String
        val request = Request(Method.GET, INVENTAIRE_API_URL + "/api/entities")
            .query("action", "by-uris")
            .query("uris", entityId)
        try {
            val response = client(request)
            val obj = JSONObject(response.body.toString())
            var volumeInfo = (obj["entities"] as JSONObject)[entityId] as JSONObject
            publisher = (volumeInfo["labels"] as JSONObject)["fr"] as String
        } catch (e: Exception) {
            println(e)
            throw e
        }
        return publisher
    }

    fun getDescription(isbn: String): String {
        val description: String
        val request = Request(Method.GET, INVENTAIRE_API_URL + "/api/data")
            .query("action", "summaries")
            .query("uri", "isbn:$isbn")
            .query("langs", "fr")
        try {
            val response = client(request)
            val obj = JSONObject(response.body.toString())
            val volumeInfo = (obj["summaries"] as JSONArray)
            description = (volumeInfo[0] as JSONObject)["text"] as String
        } catch (e: Exception) {
            println(e)
            throw e
        }
        return description
    }
}