package ch.wngr.bookstore.services

import ch.wngr.bookstore.converters.toScrapperBook
import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.repositories.BookRepository
import ch.wngr.bookstore.repositories.PublisherRepository
import ch.wngr.bookstore.scrapers.ScraperInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScraperServiceImpl @Autowired constructor(
    val scraperServices: List<ScraperInterface>,
    val bookRepository: BookRepository,
    val publisherRepository: PublisherRepository,
) : ScraperService {
    override fun getBookInfo(isbn: String): ScraperBook {
        // first check if we have the book already in stock
        val book: Book?
        var scraperBook: ScraperBook
        book = bookRepository.findByIsbn(isbn)
        if (book != null) {
            scraperBook = book.toScrapperBook()
            // Maybe more info could be found
            if (!scraperBook.isBookComplete()) {
                scraperBook = findInfos(scraperBook)
            }
        } else {
            scraperBook = ScraperBook(
                isbn = isbn,
                title = "",
                authors = arrayListOf(),
                editor = null,
                distributor = null,
                description = "",
                price = null,
                coverUrl = "",
                hasCover = false,
                amount = 0,
                tags = arrayListOf()
            )
            scraperBook = findInfos(scraperBook)
            // we try to guess the distributor
            val publisher = scraperBook.editor?.let { publisherRepository.findByName(it) }
            scraperBook.distributor = publisher?.defaultDistributor?.toString()
        }
        return scraperBook
    }

    private fun findInfos(book: ScraperBook): ScraperBook {
        for (service in scraperServices) {
            val scraperBook = service.getBookInfo(book.isbn)
            book.mergeScraperBooks(scraperBook)
            if (book.isBookComplete()) {
                return book
            }
        }
        return book
    }


}