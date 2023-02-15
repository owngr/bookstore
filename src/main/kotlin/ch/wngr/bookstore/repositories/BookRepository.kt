package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Book
import ch.wngr.bookstore.models.Inventory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface BookRepository : PagingAndSortingRepository<Book, Int> {
    fun findByIsbn(isbn: String): Book?
    fun findByTitle(title: String): Book?
    fun findByHasCover(hasCover: Boolean): List<Book>
    fun findByAmountGreaterThan(amount: Int, pageable: Pageable): Page<Book>

    @Query("select distinct b from Book b left join b.authors a " +
            "where b.amount > :amount " +
            "and (b.isbn like :searchFilter or upper(b.title) like :searchFilter or upper(a.name) like :searchFilter or upper(b.publisher.name) like :searchFilter)")
    fun findByAmountGreatherThanAndSearchFilter(amount: Int, searchFilter: String, pageable: Pageable): Page<Book>

    @Query("select new ch.wngr.bookstore.models.Inventory(sum(b.amount), sum(b.price*b.amount)) from Book b")
    fun getInventory(): Inventory

    @Modifying
    @Transactional
    @Query("update Book set amount = 0")
    fun resetStock()
}
