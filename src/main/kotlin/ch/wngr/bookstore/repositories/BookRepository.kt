package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.models.Inventory
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface BookRepository : CrudRepository<Book, Int> {
    fun findByIsbn(isbn: String): Book?
    fun findByTitle(title: String): Book?
    fun findByHasCover(hasCover: Boolean): List<Book>
    fun findByAmountGreaterThan(amount: Int): List<Book>

    @Query("select new ch.wngr.bookstore.models.Inventory(sum(b.amount), sum(b.price*b.amount)) from Book b")
    fun getInventory(): Inventory

    @Modifying
    @Transactional
    @Query("update Book set amount = 0")
    fun resetStock()
}
