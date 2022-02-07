package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Stock
import ch.wngr.bookstore.models.Inventory
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StockRepository : CrudRepository<Stock, Int> {
    fun findByBook_Id(id: Int): Stock?
    fun findByAmountGreaterThan(amount: Int): List<Stock>
    fun findByBook_HasCover(boolean: Boolean): List<Stock>

    @Query("select new ch.wngr.bookstore.models.Inventory(sum(s.amount), sum(b.price*s.amount)) from Stock s join Book b on b.id = s.book.id")
    fun getInventory(): Inventory
}
