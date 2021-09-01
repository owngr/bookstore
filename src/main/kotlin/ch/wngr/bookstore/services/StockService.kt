package ch.wngr.bookstore.services

import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.models.StockEntry
import org.springframework.stereotype.Service

@Service
interface StockService {

    fun addBook(book: ScraperBook)
    fun getStock(): List<StockEntry>
    fun getEditors(): List<String>
    fun getBookInfo(isbn: String) : ScraperBook
}
