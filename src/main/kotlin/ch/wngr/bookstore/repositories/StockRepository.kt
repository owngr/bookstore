package ch.wngr.bookstore.repositories

import ch.wngr.bookstore.entities.Stock
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StockRepository : CrudRepository<Stock, Int> {
    fun findByBook_Id(id: Int): Stock?
    fun findByAmountGreaterThan(amount: Int): List<Stock>
}
