package ch.wngr.bookstore.services

import ch.wngr.bookstore.filters.TableSearchFilter
import ch.wngr.bookstore.models.*
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
interface StockService {

    fun addBook(book: ScraperBook)
    fun getStock(page: TableSearchFilter): Page<StockEntry>
    fun getEditors(): List<Editor>
    fun getBookInfo(isbn: String): ScraperBook
    fun updateStock(stockEntry: StockEntry): StockEntry
    fun getInventory(): Inventory
    fun deleteStock()
    fun getStockEntry(isbn: String): StockEntry
    fun checkMissingBooks(saleDTOs: List<SaleDTO>): ResponseEntity<List<StockEntry>>
    fun getMissingBooks(saleDTOs: List<SaleDTO>): ArrayList<StockEntry>
    fun removeBooks(saleDTOs: List<SaleDTO>)
}
