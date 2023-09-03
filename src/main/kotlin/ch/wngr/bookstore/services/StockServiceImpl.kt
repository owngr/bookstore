package ch.wngr.bookstore.services

import ch.wngr.bookstore.converters.toEditor
import ch.wngr.bookstore.converters.toScrapperBook
import ch.wngr.bookstore.converters.toTag

import ch.wngr.bookstore.entities.Author
import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.entities.Publisher
import ch.wngr.bookstore.filters.TableSearchFilter
import ch.wngr.bookstore.models.*
import ch.wngr.bookstore.repositories.BookRepository
import ch.wngr.bookstore.repositories.PublisherRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class StockServiceImpl @Autowired constructor(
    val bookRepository: BookRepository,
    val authorService: AuthorService,
    val publisherRepository: PublisherRepository,
    val distributorService: DistributorService,
    val publisherService: PublisherService,
    val coverService: CoverService,

    ) : StockService {

    override fun addBook(book: ScraperBook) {
        // check if book info already exist and add it if not
        val existingBook: Book? = if (book.isbn.isNotEmpty()) {
            bookRepository.findByIsbn(book.isbn)
        } else {
            bookRepository.findByTitle(book.title)
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
                tags = book.tags.map(TagDto::toTag).toMutableSet()
            )
            bookRepository.save(newBook)
        } else {
            if (book.coverUrl.isNotEmpty()) {
                coverService.fetchAndUploadCover(book.coverUrl, book.isbn)
                book.hasCover = true
            }
            book.amount += existingBook.amount
            updateStock(book)
        }
    }

    override fun getStock(page: TableSearchFilter): Page<ScraperBook> {
        val pageable = PageRequest.of(page.first, page.rows, Sort.by(page.getSortDirection(), page.sortField))
        val tagsCount = page.filters.tags.value.size.toLong()
        var amount = 0
        if (page.filters.displayEmptyEntries.value) {
            amount = -1
        }
        val books: Page<ScraperBook> = if (page.filters.global.value == null && tagsCount == 0L) {
            bookRepository.findByAmountGreaterThan(amount, pageable).map(Book::toScrapperBook)
        } else if (tagsCount == 0L) {
            bookRepository.findByAmountGreatherThanAndSearchFilter(
                amount,
                "%${page.filters.global.value?.uppercase()}%",
                pageable
            ).map(Book::toScrapperBook)
        } else if (page.filters.global.value == null) {
            bookRepository.findByAmountGreatherThanAndTagFilter(
                amount, page.filters.tags.value.map(TagDto::toTag),
                tagsCount, pageable
            ).map(Book::toScrapperBook)
        } else {
            bookRepository.findByAmountGreatherThanAndSearchFilterAndTagFilter(
                amount,
                "%${page.filters.global.value.uppercase()}%",
                page.filters.tags.value.map(TagDto::toTag),
                tagsCount,
                pageable
            ).map(Book::toScrapperBook)
        }
        return books
    }

    override fun getEditors(): List<Editor> {
        return publisherRepository.findByOrderByName().map(Publisher::toEditor)
    }

    override fun updateStock(stockEntry: ScraperBook): ScraperBook {
        val book = bookRepository.findByIsbn(stockEntry.isbn)
        if (book != null) {
            book.amount = stockEntry.amount
            book.isbn = stockEntry.isbn
            val authors: MutableSet<Author> = HashSet()
            for (authorName: String in stockEntry.authors) {
                val author: Author = authorService.getOrCreateAuthor(authorName)
                authors.add(author)
            }
            book.authors = authors
            book.title = stockEntry.title
            book.description = stockEntry.description
            book.publisher =
                stockEntry.editor?.let { publisherService.getOrCreatePublisher(it, stockEntry.distributor) }
            book.distributor = stockEntry.distributor?.let { distributorService.getOrCreateDistributor(it) }
            book.price = stockEntry.price
            // When we update a book the hasCover from vue might be false
            if (stockEntry.hasCover) {
                book.hasCover = true
            }
            book.tags = stockEntry.tags.map(TagDto::toTag).toMutableSet()
            bookRepository.save(book)
            return book.toScrapperBook()
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

    override fun getStockEntry(isbn: String): ScraperBook {
        val book = bookRepository.findByIsbn(isbn)
        if (book != null) {
            return book.toScrapperBook()
        }
        println("The book could not be found in stock")
        throw NotFoundException("The book could not be found in stock")
    }

    override fun checkMissingBooks(saleDTOs: List<SaleDTO>): ResponseEntity<List<ScraperBook>> {
        val missingBooks = getMissingBooks(saleDTOs)
        if (missingBooks.isEmpty()) {
            return ResponseEntity(null, HttpStatus.OK)
        }
        return ResponseEntity(missingBooks, HttpStatus.CONFLICT)
    }


    override fun getMissingBooks(saleDTOs: List<SaleDTO>): ArrayList<ScraperBook> {
        val reducedSales = HashMap<String, Int>()
        for (sale in saleDTOs) {
            // fanzines aren't in stock
            if (sale.isbn.isNullOrEmpty()) {
                continue
            }
            if (reducedSales[sale.isbn] == null) {
                reducedSales[sale.isbn] = sale.quantity!!.or(0)
            } else {
                reducedSales.replace(sale.isbn, reducedSales[sale.isbn]!! + sale.quantity!!.or(0))
            }
        }
        val missingBooks = ArrayList<ScraperBook>()
        for (sale in reducedSales) {
            val book = bookRepository.findByIsbn(sale.key)
            if (book == null) {
                throw NotFoundException(String.format("The book with isbn %s could not be found in stock", sale.key))
            } else if (book.amount < sale.value) {
                missingBooks.add(book.toScrapperBook())
            }
        }
        return missingBooks
    }

    override fun removeBooks(saleDTOs: List<SaleDTO>) {
        for (sale in saleDTOs) {
            if (!sale.isbn.isNullOrEmpty()) {
                val book = bookRepository.findByIsbn(sale.isbn)
                if (book != null) {
                    book.amount -= sale.quantity!!.or(0)
                    bookRepository.save(book)
                } else {
                    throw NotFoundException(
                        String.format(
                            "The book with isbn %s could not be found in stock",
                            sale.isbn
                        )
                    )
                }
            }

        }
    }

}
