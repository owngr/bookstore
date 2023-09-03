package ch.wngr.bookstore.controllers

import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.services.CoverService
import ch.wngr.bookstore.services.ScraperService
import ch.wngr.bookstore.services.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/book")
class BookController @Autowired constructor(
    private val stockService: StockService,
    private val coverService: CoverService,
    private val scraperService: ScraperService
) {
    @GetMapping("/ISBN")
    fun fetchBookInfo(isbn: String): ScraperBook {
        return scraperService.getBookInfo(isbn)
    }

    @PostMapping("/stock")
    fun addBookToStock(@RequestBody book: ScraperBook) {
        return stockService.addBook(book)
    }

//    @PostMapping("/cover")
//    fun addCover(@RequestBody cover: Cover) {
//        return coverService.uploadBookCover(cover.isbn, cover.image)
//    }

    @PostMapping("/cover")
    fun addCover(@RequestParam("file") cover: MultipartFile, @RequestParam("isbn") isbn: String) {
        return coverService.uploadBookCover(cover, isbn)
    }
}
