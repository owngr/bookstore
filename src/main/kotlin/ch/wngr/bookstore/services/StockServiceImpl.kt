package ch.wngr.bookstore.services

import ch.wngr.bookstore.entities.Author
import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.entities.Publisher
import ch.wngr.bookstore.entities.Stock
import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.models.StockEntry
import ch.wngr.bookstore.repositories.AuthorRepository
import ch.wngr.bookstore.repositories.BookRepository
import ch.wngr.bookstore.repositories.PublisherRepository
import ch.wngr.bookstore.repositories.StockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StockServiceImpl @Autowired constructor(
    val authorRepository: AuthorRepository,
    val bookRepository: BookRepository,
    val stockRepository: StockRepository,
    val publisherRepository: PublisherRepository,
    val scraperService: ScraperService,

) : StockService {

    override fun addBook(book: ScraperBook) {
        // check if book info already exist and add it if not
        var existingBook: Book?
        if (!book.isbn.isEmpty()) {
            existingBook = bookRepository.findByIsbn(book.isbn)
        } else {
            existingBook = bookRepository.findByTitle(book.title)
        }
        if (existingBook == null) {
            val authors: MutableSet<Author> = HashSet()
            for (authorName: String in book.authors) {
                var author: Author? = authorRepository.findByName(authorName)
                if (author == null) {
                    author = Author(authorName)
                    author = authorRepository.save(author)
                }
                authors.add(author)
            }
            val publisher = book.editor?.let { getOrCreatePublisher(it) }
            val newBook = Book(book.isbn, book.title, authors = authors, description = book.description, publisher = publisher)
            existingBook = bookRepository.save(newBook)
        }
        // add the book to the stock
        var stock: Stock?
        stock = stockRepository.findByBook_Id(existingBook.id)
        if (stock != null) {
            stock.amount = stock.amount + 1
        } else {
            stock = Stock(1, book = existingBook)
        }
        stockRepository.save(stock)
    }

    fun getOrCreatePublisher(publisher: String): Publisher {
        var existingPublisher: Publisher?
        existingPublisher = publisherRepository.findByName(publisher)
        if (existingPublisher == null) {
            existingPublisher = Publisher(name = publisher)
            existingPublisher = publisherRepository.save(existingPublisher)
        }
        return existingPublisher
    }

    override fun getStock(): List<StockEntry> {
        val stockEntryList: MutableList<StockEntry> = ArrayList()
        var stockEntry: StockEntry
        val stock: List<Stock> = stockRepository.findByAmountGreaterThan(0)
        for (stockEnt: Stock in stock) {
            stockEntry = StockEntry(
                isbn = stockEnt.book.isbn,
                title = stockEnt.book.title,
                authors = stockEnt.book.authors.map(
                    fun(author: Author): String {
                        return author.name
                    }
                ).toList(),
                editor = stockEnt.book.publisher?.name,
                distributor = null,
                amount = stockEnt.amount,
                description = stockEnt.book.description,
            )
            stockEntryList.add(stockEntry)
        }
        return stockEntryList
    }

    override fun getEditors(): List<String> {
        return publisherRepository.findAll().toList().map(Publisher::toString)
    }

    override fun getBookInfo(isbn: String): ScraperBook {
        // first check if we have the book already in stock
        val book: Book?
        val scraperBook: ScraperBook
        book = bookRepository.findByIsbn(isbn)
        if (book != null) {
            scraperBook = ScraperBook(
                isbn = isbn,
                title = book.title,
                authors = book.authors.map(Author::toString),
                editor = book.publisher.toString(),
                distributor = "",
                description = book.description,

            )
        } else {
            scraperBook = scraperService.getBookInfo(isbn)
        }
        return scraperBook
    }
}
