package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.services.ScraperService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/book")
class BookController @Autowired constructor(
    private val scraperService: ScraperService
) {
    @GetMapping("/ISBN")
    fun fetchBookInfo(isbn: String): ScraperBook {
        return scraperService.getBookInfo(isbn);
    }
}