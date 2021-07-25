package ch.wngr.bookstore.services

import ch.wngr.bookstore.models.ScraperBook
import khttp.responses.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.lang.Exception


@Service
class GoogleScraperService : ScraperService {
    val GOOGLE_API_URL = "https://www.googleapis.com/books/v1/volumes";

    override fun getBookInfo(isbn: String): ScraperBook {
        try {
            val response: Response = khttp.get(
                url = GOOGLE_API_URL,
                params = mapOf("q" to "isbn:$isbn")
            );
            val obj: JSONObject = response.jsonObject
            val volumeInfo: JSONObject = (obj["items"] as JSONArray).getJSONObject(0)["volumeInfo"] as JSONObject
            val title: String = volumeInfo["title"] as String
            var publisher = "";
            try {
                publisher = volumeInfo["publisher"] as String
            } catch (e: JSONException) {
                println("publisher not found")
            }
            val authors: List<String> = jsonArrayToStringList(volumeInfo["authors"] as JSONArray)
            return ScraperBook(
                isbn = isbn,
                title = title,
                authors = authors,
                editor = publisher,
                distributor = "",
            );
        } catch (e: Exception) {
            println(e)
            throw e
        }


    }


    companion object {
        fun jsonArrayToStringList(jsonArray: JSONArray): ArrayList<String> {
            val list: ArrayList<String> = arrayListOf();
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray[i] as String)
            }
            return list
        }
    }


}