package ch.wngr.bookstore.services

import ch.wngr.bookstore.converters.toEditor
import ch.wngr.bookstore.converters.toScrapperBook
import ch.wngr.bookstore.converters.toStockEntry
import ch.wngr.bookstore.entities.Author
import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.entities.Publisher
import ch.wngr.bookstore.entities.Stock
import ch.wngr.bookstore.models.Editor
import ch.wngr.bookstore.models.Inventory
import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.models.StockEntry
import ch.wngr.bookstore.repositories.BookRepository
import ch.wngr.bookstore.repositories.PublisherRepository
import ch.wngr.bookstore.repositories.StockRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StockServiceImpl @Autowired constructor(
    val bookRepository: BookRepository,
    val stockRepository: StockRepository,
    val scraperService: ScraperService,
    val authorService: AuthorService,
    val publisherRepository: PublisherRepository,
    val distributorService: DistributorService,
    val publisherService: PublisherService,
    val coverService: CoverService,

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
                val author: Author = authorService.getOrCreateAuthor(authorName)
                authors.add(author)
            }
            val publisher = book.editor?.let { publisherService.getOrCreatePublisher(it, book.distributor) }
            val distributor = book.distributor?.let { distributorService.getOrCreateDistributor(it) }
            var hasCover = false
            if (book.coverUrl.isNotEmpty()) {
                coverService.fetchAndUploadCover(book.coverUrl, book.isbn)
                hasCover = true
            }
            val newBook = Book(
                isbn = book.isbn,
                title = book.title,
                authors = authors,
                description = book.description,
                publisher = publisher,
                distributor = distributor,
                price = book.price,
                hasCover = hasCover,
            )
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

    override fun getStock(): List<StockEntry> {
        val stockEntryList: MutableList<StockEntry> = ArrayList()
        var stockEntry: StockEntry
        val stock: List<Stock> = stockRepository.findByAmountGreaterThan(0)
        for (stockEnt: Stock in stock) {
            stockEntry = stockEnt.toStockEntry()
            stockEntryList.add(stockEntry)
        }
        return stockEntryList
    }

    override fun getEditors(): List<Editor> {
        return publisherRepository.findAll().toList().map(Publisher::toEditor)
    }

    override fun getBookInfo(isbn: String): ScraperBook {
        // first check if we have the book already in stock
        val book: Book?
        val scraperBook: ScraperBook
        book = bookRepository.findByIsbn(isbn)
        if (book != null) {
            scraperBook = book.toScrapperBook()
        } else {
            scraperBook = scraperService.getBookInfo(isbn)
            // we try to guess the distributor
            val publisher = scraperBook.editor?.let { publisherRepository.findByName(it) }
            scraperBook.distributor = publisher?.defaultDistributor?.toString()
        }
        return scraperBook
    }

    override fun updateStock(stockEntry: StockEntry): StockEntry {
        val book = bookRepository.findByIsbn(stockEntry.isbn)
        val stock = book?.let { stockRepository.findByBook_Id(it.id) }
        if (stock != null) {
            stock.amount = stockEntry.amount!!
            stockRepository.save(stock)
        }
        if (book != null) {
            book.isbn = stockEntry.isbn
            val authors: MutableSet<Author> = HashSet()
            for (authorName: String in stockEntry.authors) {
                val author: Author = authorService.getOrCreateAuthor(authorName)
                authors.add(author)
            }
            book.authors = authors
            book.title = stockEntry.title
            book.description = stockEntry.description!!
            book.publisher = stockEntry.editor?.let { publisherService.getOrCreatePublisher(it, stockEntry.distributor) }
            book.distributor = stockEntry.distributor?.let { distributorService.getOrCreateDistributor(it) }
            book.price = stockEntry.price
            bookRepository.save(book)
            return stockRepository.findByBook_Id(book.id)!!.toStockEntry()
        } else {
            println("The book could not be found")
            throw NotFoundException("The book could not be found")
        }
    }

    override fun getInventory(): Inventory {
        return stockRepository.getInventory()
    }

    override fun deleteStock() {
        return stockRepository.deleteAll()
    }
}
