package ch.wngr.bookstore.services

import ch.wngr.bookstore.converters.toEditor
import ch.wngr.bookstore.converters.toScrapperBook
import ch.wngr.bookstore.converters.toStockEntry
import ch.wngr.bookstore.entities.Author
import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.entities.Publisher
import ch.wngr.bookstore.models.*
import ch.wngr.bookstore.repositories.BookRepository
import ch.wngr.bookstore.repositories.PublisherRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class StockServiceImpl @Autowired constructor(
    val bookRepository: BookRepository,
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
                amount = book.amount,
            )
            bookRepository.save(newBook)
        } else {
            existingBook.amount += book.amount
            bookRepository.save(existingBook)
        }
    }

    override fun getStock(): List<StockEntry> {
        val stockEntryList: MutableList<StockEntry> = ArrayList()
        var stockEntry: StockEntry
        val books: List<Book> = bookRepository.findByAmountGreaterThan(0)
        for (stockEnt: Book in books) {
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
        if (book != null) {
            book.amount = stockEntry.amount!!
            book.isbn = stockEntry.isbn
            val authors: MutableSet<Author> = HashSet()
            for (authorName: String in stockEntry.authors) {
                val author: Author = authorService.getOrCreateAuthor(authorName)
                authors.add(author)
            }
            book.authors = authors
            book.title = stockEntry.title
            book.description = stockEntry.description!!
            book.publisher =
                stockEntry.editor?.let { publisherService.getOrCreatePublisher(it, stockEntry.distributor) }
            book.distributor = stockEntry.distributor?.let { distributorService.getOrCreateDistributor(it) }
            book.price = stockEntry.price
            bookRepository.save(book)
            return book.toStockEntry()
        } else {
            println("The book could not be found")
            throw NotFoundException("The book could not be found")
        }
    }

    override fun getInventory(): Inventory {
        return bookRepository.getInventory()
    }

    override fun deleteStock() {
        return bookRepository.resetStock()
    }

    override fun getStockEntry(isbn: String): StockEntry {
        val book = bookRepository.findByIsbn(isbn)
        if (book != null) {
            return book.toStockEntry()
        }
        println("The book could not be found in stock")
        throw NotFoundException("The book could not be found in stock")
    }

    override fun checkMissingBooks(saleDTOS: List<SaleDTO>): ResponseEntity<List<StockEntry>> {
        val missingBooks = getMissingBooks(saleDTOS)
        if (missingBooks.isEmpty()) {
            return ResponseEntity(null, HttpStatus.OK)
        }
        return ResponseEntity(missingBooks, HttpStatus.CONFLICT)
    }


    override fun getMissingBooks(saleDTOS: List<SaleDTO>): ArrayList<StockEntry> {
        val reducedSales = HashMap<String, Int>()
        for (sale in saleDTOS) {
            if (reducedSales[sale.isbn] == null) {
                reducedSales[sale.isbn] = sale.quantity!!.or(0)
            } else {
                reducedSales.replace(sale.isbn, reducedSales.get(sale.isbn)!! + sale.quantity!!.or(0))
            }
        }
        val missingBooks = ArrayList<StockEntry>()
        for (sale in reducedSales) {
            val book = bookRepository.findByIsbn(sale.key)
            if (book == null) {
                throw NotFoundException(String.format("The book with isbn %s could not be found in stock", sale.key))
            } else if (book.amount < sale.value) {
                missingBooks.add(book.toStockEntry())
            }
        }
        return missingBooks
    }

    override fun removeBooks(saleDTOS: List<SaleDTO>) {
        for (sale in saleDTOS) {
            val book = bookRepository.findByIsbn(sale.isbn)
            if (book != null) {
                book.amount -= sale.quantity!!.or(0)
                bookRepository.save(book)
            } else {
                throw NotFoundException(String.format("The book with isbn %s could not be found in stock", sale.isbn))
            }
        }
    }

}
