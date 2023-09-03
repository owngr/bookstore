package ch.wngr.bookstore.scrapers

import ch.wngr.bookstore.models.ScraperBook
import org.springframework.stereotype.Service

@Service
interface ScraperInterface {

    fun getBookInfo(isbn: String): ScraperBook
}