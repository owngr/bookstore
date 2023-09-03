package ch.wngr.bookstore.services

import ch.wngr.bookstore.models.ScraperBook

interface ScraperService {

    fun getBookInfo(isbn: String): ScraperBook
}