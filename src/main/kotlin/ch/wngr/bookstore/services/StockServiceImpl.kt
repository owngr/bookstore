package ch.wngr.bookstore.services

import ch.wngr.bookstore.entities.Author
import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.entities.Stock
import ch.wngr.bookstore.models.ScraperBook
import ch.wngr.bookstore.repositories.AuthorRepository
import ch.wngr.bookstore.repositories.BookRepository
import ch.wngr.bookstore.repositories.StockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StockServiceImpl @Autowired constructor(
    val authorRepository: AuthorRepository,
    val bookRepository: BookRepository,
    val stockRepository: StockRepository

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
            val newBook = Book(book.isbn, book.title, authors = authors)
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
}
