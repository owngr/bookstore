package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Book
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : CrudRepository<Book, Int> {
    fun findByIsbn(isbn: String): Book?
    fun findByTitle(title: String): Book?
}
