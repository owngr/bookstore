package ch.wngr.bookstore.services

import ch.wngr.bookstore.filters.TableSearchFilter
import ch.wngr.bookstore.models.*
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
interface StockService {

    fun addBook(book: ScraperBook)
    fun getStock(page: TableSearchFilter): Page<ScraperBook>
    fun getEditors(): List<Editor>
    fun updateStock(stockEntry: ScraperBook): ScraperBook
    fun getInventory(): Inventory
    fun deleteStock()
    fun getStockEntry(isbn: String): ScraperBook
    fun checkMissingBooks(saleDTOs: List<SaleDTO>): ResponseEntity<List<ScraperBook>>
    fun getMissingBooks(saleDTOs: List<SaleDTO>): ArrayList<ScraperBook>
    fun removeBooks(saleDTOs: List<SaleDTO>)
}
