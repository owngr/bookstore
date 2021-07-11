package ch.wngr.bookstore.services

import ch.wngr.bookstore.models.ScraperBook
import org.springframework.stereotype.Service

@Service
interface ScraperService {

    fun getBookInfo(isbn: String): ScraperBook;
}