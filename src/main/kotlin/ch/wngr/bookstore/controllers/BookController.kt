package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.services.ScraperService
import ch.wngr.bookstore.services.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/book")
class BookController @Autowired constructor(
    private val scraperService: ScraperService,
    private val stockService: StockService,
) {
    @GetMapping("/ISBN")
    fun fetchBookInfo(isbn: String): ScraperBook {
        return scraperService.getBookInfo(isbn)
    }

    @PostMapping("/stock")
    fun addBookToStock(@RequestBody book: ScraperBook) {
        return stockService.addBook(book)
    }
}
